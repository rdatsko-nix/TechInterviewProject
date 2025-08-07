class CounterLogger {

    var counter = 0
    val logs = mutableListOf<Int>()

    fun increment() {
        val newValue = counter + 1
        counter = newValue
        logs.add(newValue)
    }
}
