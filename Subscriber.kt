/**
 * 订阅
 *
 * @author lei.chuguang Email:475825657@qq.com
 * @version 1.0
 * @since 2019/5/22 20:17
 */
object Subscriber {
    @JvmStatic
    private var observers = arrayListOf<ReceiverImp<*>>()

    /**订阅*/
    @JvmStatic
    fun <T> subscribe(receiver: Receiver<T>) {
        observers.add(ReceiverImp(receiver))
    }

    /**取消订阅*/
    @JvmStatic
    fun <T> unsubscribe(receiver: Receiver<T>) {
        observers.remove(ReceiverImp(receiver))
    }

    /**发送数据*/
    @JvmStatic
    fun send(data: Any) {
        for (observer in observers) {
            val clazz = observer.receiver.javaClass
            clazz.methods.forEach {
                val parameterTypes = it.parameterTypes
                if ("receive" == it.name && parameterTypes.size == 1 && data.javaClass == parameterTypes[0]) {
                    val receive = observer.receive(data)
                    if (receive) return
                }
            }
        }
    }
}

/**接收器
 */
interface Receiver<T> {
    /**接收数据
     *@return 是否中断
     */
    fun receive(data: T): Boolean
}

/**接收器代理
 */
private class ReceiverImp<T>(val receiver: Receiver<T>) {
    /**接收数据
     * @param data kotlin的安全特性，这里设置data为Any类型
     *@return 是否中断
     */
    fun receive(data: Any): Boolean {
        val t = data as? T ?: return true
        return receiver.receive(t)
    }
}
