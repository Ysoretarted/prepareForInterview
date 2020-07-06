## 回调

### 设计原理
注意： 一个类有有参构造，一定要再加一个无参构造

- Client 实现了 MyCallBack
- Server 的 reponse方法可以接受一个MyCallBack参数， 并**调用了MyCallBack的process()**
- Client的包含Server的一个引用，并调用reponse(this)方法,通过this把自己传进去


