#Thread-Specific Storage模式
	Specific是"特定的"的意思，Storage是储存柜、存储装置的意思。因此，所谓Thread-Specific Storage就是"每个线程持有的存储柜" "为每个线程准备的存储空间"的意思。
	Thread-Specific Storage模式是一种即使只有一个入口，也会在内部为每个线程分配持有的存储空间的模式。
