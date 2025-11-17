
### v2.0.3

* 修复 lpc 被代理包装后，不能识别方法参数注解的问题

### v2.0.2

* 优化 lpc 异常处理，确保外层收到的总会是 DamiException

### v2.0.1

* 添加 bus ReceivablePayload:onError 用于监听异常（可以转给接收器 skin）
* 添加 lpc CoderForIndex，CoderForName，且 CoderDefault 标为弃用
* 优化 lpc ConsumerInvocationHandler 与回调异常的兼容性（之前只管上抛异常）

### v2.0.0

