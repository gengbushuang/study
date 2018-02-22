package org.raft.common.db;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.GZIPOutputStream;

import org.raft.common.message.LogEntry;

public class H2LogStore {
	// 表名
	private static final String TABLE_NAME = "LogStore";
	// 创建自增的索引，从1开始，递增量为1
	private static final String CREATE_SEQUENCE_SQL = "CREATE SEQUENCE LogSequence START WITH 1 INCREMENT BY 1";
	// 创建表
	private static final String CREATE_TABLE_SQL = "CREATE TABLE LogStore(id bigint default LogSequence.nextval primary key, term bigint, dtype tinyint, data blob)";
	// 插入数据
	private static final String INSERT_ENTRY_SQL = "INSERT INTO LogStore(term, dtype, data) values(?, ?, ?)";
	// 更新数据
	private static final String UPDATE_ENTRY_SQL = "UPDATE LogStore SET term=?,dtype=?,data=? WHERE id=?";
	// 更新起始的序列ID
	private static final String UPDATE_SEQUENCE_SQL = "ALTER SEQUENCE LogSequence RESTART WITH ? INCREMENT BY 1";
	// 删除大于当前ID的数据
	private static final String TRIM_TABLE_SQL = "DELETE FROM LogStore WHERE id > ?";
	// 获取ID范围的集合日志
	private static final String SELECT_RANGE_SQL = "SELECT * FROM LogStore WHERE id >= ? AND id < ?";
	// 查询某个ID日志
	private static final String SELECT_ENTRY_SQL = "SELECT * FROM LogStore WHERE id=?";

	// 起始索引
	private AtomicLong startIndex;
	// 下一个索引
	private AtomicLong nextIndex;

	private LogEntry lastEntry;

	private Connection connection;

	public H2LogStore(String path) {
		this.startIndex = new AtomicLong();
		this.nextIndex = new AtomicLong();

		this.lastEntry = new LogEntry(0, null);

		try {
			Class.forName("org.h2.Driver");
			this.connection = DriverManager.getConnection("jdbc:h2:" + path, "sa", "");
			// 关闭自动提交
			this.connection.setAutoCommit(false);
			boolean isNew = true;
			// 查询所有的表
			ResultSet tables = this.connection.createStatement().executeQuery("SHOW TABLES");
			while (tables.next()) {
				if (TABLE_NAME.equalsIgnoreCase(tables.getString(1))) {
					isNew = false;
					break;
				}
			}
			tables.close();
			if (isNew) {
				// 创建自增序列
				this.connection.createStatement().execute(CREATE_SEQUENCE_SQL);
				// 创建表
				this.connection.createStatement().execute(CREATE_TABLE_SQL);
				// 提交
				this.connection.commit();
				this.startIndex.set(1);
				this.nextIndex.set(1);
			} else {
				// 查询表里面的最小id和最大id
				ResultSet rs = this.connection.createStatement().executeQuery("SELECT MIN(id), MAX(id) FROM LogStore");
				if (rs.next()) {
					// 起始ID等于最小ID
					this.startIndex.set(rs.getLong(1));
					// 下一个ID等于最大ID＋1
					this.nextIndex.set(rs.getLong(2) + 1);
				} else {
					this.startIndex.set(1);
					this.nextIndex.set(1);
				}
				rs.close();
				// 获取表根据ID降序第一行记录，ID最大值的第一行
				rs = this.connection.createStatement().executeQuery("SELECT TOP 1 * FROM LogStore ORDER BY id DESC");
				if (rs.next()) {
					this.lastEntry = new LogEntry(rs.getLong(2), rs.getBytes(4));
				}

				rs.close();
			}

		} catch (Throwable error) {
			error.printStackTrace();
			throw new RuntimeException("failed to load or create a log store", error);
		}
	}

	// 下个ID的值
	public long getFirstAvailableIndex() {
		return this.nextIndex.get();
	}

	// 起始ID值
	public long getStartIndex() {
		return this.startIndex.get();
	}

	// 日志内容
	public LogEntry getLastLogEntry() {
		return this.lastEntry;
	}

	// 追加日志
	public long append(LogEntry logEntry) {
		try {
			PreparedStatement ps = this.connection.prepareStatement(INSERT_ENTRY_SQL);
			ps.setLong(1, logEntry.getTerm());
			ps.setByte(2, (byte) 0);
			ps.setBytes(3, logEntry.getValue());
			ps.execute();
			this.connection.commit();
			this.lastEntry = logEntry;
			// 先返回在加1
			return this.nextIndex.getAndIncrement();
		} catch (Exception error) {
			throw new RuntimeException("log store error", error);
		}
	}

	// 更新日志
	public void writeAt(long index, LogEntry logEntry) {
		if (index >= this.nextIndex.get() || index < this.startIndex.get()) {
			throw new IllegalArgumentException("index out of range");
		}

		try {
			PreparedStatement ps = this.connection.prepareStatement(UPDATE_ENTRY_SQL);
			ps.setLong(1, logEntry.getTerm());
			ps.setByte(2, (byte) 0);
			ps.setBytes(3, logEntry.getValue());
			ps.setLong(4, index);
			ps.execute();
			// 删除大于Index值的数据
			ps = this.connection.prepareStatement(TRIM_TABLE_SQL);
			ps.setLong(1, index);
			ps.execute();
			// 更新Index的序列
			ps = this.connection.prepareStatement(UPDATE_SEQUENCE_SQL);
			ps.setLong(1, index + 1);
			ps.execute();
			this.connection.commit();
			this.nextIndex.set(index + 1);
			this.lastEntry = logEntry;

		} catch (Exception error) {
			throw new RuntimeException("log store error", error);
		}
	}

	// 获取日志集合信息
	public LogEntry[] getLogEntries(long start, long end) {
		if (start > end || start < this.startIndex.get()) {
			throw new IllegalArgumentException("index out of range");
		}

		try {
			PreparedStatement ps = this.connection.prepareStatement(SELECT_RANGE_SQL);
			ps.setLong(1, start);
			ps.setLong(2, end);

			ResultSet rs = ps.executeQuery();
			List<LogEntry> entries = new ArrayList<LogEntry>();
			while (rs.next()) {
				entries.add(new LogEntry(rs.getLong(2), rs.getBytes(4)));
			}
			rs.close();

			return entries.toArray(new LogEntry[0]);
		} catch (Exception error) {
			throw new RuntimeException("log store error", error);
		}

	}

	// 获取某个日志
	public LogEntry getLogEntryAt(long index) {
		if (index < this.startIndex.get()) {
			throw new IllegalArgumentException("index out of range");
		}

		try {
			PreparedStatement ps = this.connection.prepareStatement(SELECT_ENTRY_SQL);
			ps.setLong(1, index);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return new LogEntry(rs.getLong(2), rs.getBytes(4));
			}
			rs.close();
			return null;
		} catch (Exception error) {
			throw new RuntimeException("log store error", error);
		}
	}

	public byte[] packLog(long index, int itemsToPack) {
		if (index < this.startIndex.get() || index > this.nextIndex.get()) {
			throw new IllegalArgumentException("index out of range");
		}

		try {
			ByteArrayOutputStream memoryStream = new ByteArrayOutputStream();
			GZIPOutputStream gzipStream = new GZIPOutputStream(memoryStream);
			PreparedStatement ps = this.connection.prepareStatement(SELECT_RANGE_SQL);
		} catch (Exception error) {
			throw new RuntimeException("log store error", error);
		}
	}
}
