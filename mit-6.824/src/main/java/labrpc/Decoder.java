package labrpc;

public interface Decoder<IN, OUT> {

    OUT decode(IN in);
}
