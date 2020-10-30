package com.zayd.rxjava_rxandroid_todo

class DataSource {
    companion object {
        fun createTaskList(): List<Task> {
            val tasks = ArrayList<Task>()
            tasks.add(Task("Take out the thrash", true, 3))
            tasks.add(Task("Walk the dog", false, 2))
            tasks.add(Task("Make my bed", true, 1))
            tasks.add(Task("Unload the dishwasher", false, 0))
            tasks.add(Task("Make Dinner", true, 5))
            return tasks
        }
    }
}