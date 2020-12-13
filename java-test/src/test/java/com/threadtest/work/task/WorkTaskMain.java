package com.threadtest.work.task;

public class WorkTaskMain {

    public void testTask1() throws InterruptedException {
        WorkTask workTask = new WorkTask();

        TaskNode userProfileTask = new TaskNode(new Task() {
            @Override
            public void run(TaskContext context) {
                System.out.println("-->userProfileTask");
            }
        });

        TaskNode userClusterTask = new TaskNode(new Task() {
            @Override
            public void run(TaskContext context) {
                System.out.println("-->userClusterTask");
            }
        });

        TaskNode retrievalTask = new TaskNode(new Task() {
            @Override
            public void run(TaskContext context) {
                System.out.println("-->retrievalTask");
            }
        });

        TaskNode rerankingTask = new TaskNode(new Task() {
            @Override
            public void run(TaskContext context) {
                System.out.println("-->rerankingTask");
            }
        });

        TaskNode responseBuildTask = new TaskNode(new Task() {
            @Override
            public void run(TaskContext context) {
                System.out.println("-->responseBuildTask");
            }
        });

        TaskNode adxTask = new TaskNode(new Task() {
            @Override
            public void run(TaskContext context) {
                System.out.println("-->adxTask");
            }
        });

        TaskNode noveltyTask = new TaskNode(new Task() {
            @Override
            public void run(TaskContext context) {
                System.out.println("-->noveltyTask");
            }
        });

        TaskNode expTask = new TaskNode(new Task() {
            @Override
            public void run(TaskContext context) {
                System.out.println("-->expTask");
            }
        });


        workTask.addStartNode(userProfileTask);
        workTask.addStartNode(userClusterTask);
        workTask.addStartNode(noveltyTask);
        workTask.addStartNode(expTask);

        workTask.addEdge(userClusterTask, retrievalTask);
        workTask.addEdge(userProfileTask, retrievalTask);

        workTask.addEdge(userProfileTask, adxTask);

        workTask.addEdge(retrievalTask, rerankingTask);
        workTask.addEdge(noveltyTask, rerankingTask);
        workTask.addEdge(adxTask, rerankingTask);
        workTask.addEdge(expTask, rerankingTask);

        workTask.addEdge(rerankingTask, responseBuildTask);
        workTask.connectToEnd(responseBuildTask);

        workTask.start(new TaskContext());
        workTask.waitDone();
    }


    public void testTask2() throws InterruptedException {
        WorkTask workTask = new WorkTask();

        TaskNode locationTask = new TaskNode(new Task() {
            @Override
            public void run(TaskContext context) {
                System.out.println("-->locationTask");
            }
        });

        TaskNode buildMixerRequestTask = new TaskNode(new Task() {
            @Override
            public void run(TaskContext context) {
                System.out.println("-->buildMixerRequestTask");
            }
        });

        TaskNode requestMixerTask = new TaskNode(new Task() {
            @Override
            public void run(TaskContext context) {
                System.out.println("-->requestMixerTask");
            }
        });

        TaskNode effectDataTask = new TaskNode(new Task() {
            @Override
            public void run(TaskContext context) {
                System.out.println("-->effectDataTask");
            }
        });

        TaskNode renderResponseTask = new TaskNode(new Task() {
            @Override
            public void run(TaskContext context) {
                System.out.println("-->renderResponseTask");
            }
        });

        TaskNode buildLayerConfigTask = new TaskNode(new Task() {
            @Override
            public void run(TaskContext context) {
                System.out.println("-->buildLayerConfigTask");
            }
        });

        workTask.addStartNode(locationTask);

        workTask.addEdge(locationTask, buildMixerRequestTask);
        workTask.addEdge(locationTask, buildLayerConfigTask);
        workTask.addEdge(buildMixerRequestTask, requestMixerTask);

        workTask.addEdge(requestMixerTask, effectDataTask);
        workTask.addEdge(effectDataTask, renderResponseTask);
        workTask.addEdge(buildLayerConfigTask, renderResponseTask);
        workTask.connectToEnd(renderResponseTask);

        workTask.start(new TaskContext());
        workTask.waitDone();
    }

    public void testTaskNumber() throws InterruptedException {
        AddTask add1 = new AddTask(1);
        AddTask add2 = new AddTask(2);

        MultiplyTask multiply = new MultiplyTask(3);

        TaskNode add1Node1 = new TaskNode(add1);
        TaskNode add2Node1 = new TaskNode(add2);
        TaskNode multiplyNode = new TaskNode(multiply);
        TaskNode printNode = new TaskNode(new PrintTask());

        TaskNode add1Node2 = new TaskNode(add1);
        TaskNode add2Node2 = new TaskNode(add2);

        WorkTask workTask = new WorkTask();
        workTask.addStartNode(add1Node1);
        workTask.addStartNode(add2Node1);

        workTask.addEdge(add1Node1, multiplyNode);
        workTask.addEdge(add2Node1, multiplyNode);

        workTask.addEdge(multiplyNode, add1Node2);
        workTask.addEdge(multiplyNode, add2Node2);

        workTask.addEdge(add1Node2, printNode);
        workTask.addEdge(add2Node2, printNode);

        workTask.connectToEnd(printNode);
        workTask.start(new NumberContext(1));
        workTask.waitDone();
    }


    public static void main(String[] args) throws InterruptedException {
//        WorkTask workTask = new WorkTask();
//        TaskNode oneTask = new TaskNode(new OneTask());
//        TaskNode twoTask = new TaskNode(new TwoTask());
//        TaskNode threeTask = new TaskNode(new ThreeTask());
//        TaskNode fixeTask = new TaskNode(new FixeTask());
//
//        workTask.addStartNode(oneTask);
//        workTask.addEdge(oneTask, twoTask);
//        workTask.addEdge(oneTask, twoTask);
//        workTask.addEdge(twoTask, threeTask);
//        workTask.addEdge(oneTask, fixeTask);
//        workTask.connectToEnd(threeTask);
//
//        workTask.start(new TaskContext());
//        System.out.println("-------------------------");
//        workTask.goStart(new TaskContext());

        WorkTaskMain workTaskMain = new WorkTaskMain();

        workTaskMain.testTask1();



    }
}
