#Balking模式
	如果现在不适合执行这个操作，或者没必要执行这个操作，就停止处理，直接返回
	Balking模式与Guarded Suspension模式一样，也存在守护条件。在Balking模式中，如果守护条件不成立，则立即中断处理。这与Guarded Suspension模式有所不同，因为Guarded Suspension模式是一直等待至可以运行。

