#Worker Thread模式
	Worker的意思是工作的人、劳动者。在Worker Thread模式中，工人线程(worker thread)会逐个取回工作并进行处理。当所有工作全部完成后，工人线程会等待新的工作到来。
	Worker Thread模式也被称为Background Thread(背景线程)模式。另外，如果从"保存多个工人线程的场所"这一点来看，我们也可以称这种模式为Thread Pool(线程池)模式。
