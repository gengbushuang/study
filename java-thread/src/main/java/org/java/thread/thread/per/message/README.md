#Thread-Per-Message模式
	所谓Per，就是"每~"的意思。因此，Thread Per Message直译过来就是"每个消息一个线程"的意思。Message在这里可以理解为"命令"或"请求"。为每个命令或请求新分配一个线程，由这个线程来执行处理——这就是Thread-Per-Message模式。
	在Thread-Per-Message模式中，消息的"委托端"和"执行端"是不同的线程。消息的委托端线程会告诉执行端线程"这项工作就交给你了"。
