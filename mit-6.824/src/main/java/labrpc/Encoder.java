package labrpc;

public interface Encoder<IN, OUT> {

    OUT encoder(IN in);
}
