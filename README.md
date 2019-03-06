# 订单拉取模块

order-puller-core   订单拉取核心模块，抽象拉取->存储->发送消息的公共逻辑。<br/>
tmall-order-puller  天猫订单拉取模块。<br/>
jd-order-puller     京东订单拉取模块。<br/>
vip-order-puller    唯品会订单拉取模块。<br/>

其他模块只需要继承OrderPuller并实现pull接口就可以完成订单拉取的任务。