# ☔️ 性能测试

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->


- [👻 内存泄漏](#-%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F)
    - [验证结果](#%E9%AA%8C%E8%AF%81%E7%BB%93%E6%9E%9C)
    - [执行方式](#%E6%89%A7%E8%A1%8C%E6%96%B9%E5%BC%8F)
- [🐎 TPS & 压力测试](#-tps--%E5%8E%8B%E5%8A%9B%E6%B5%8B%E8%AF%95)
    - [验证结果](#%E9%AA%8C%E8%AF%81%E7%BB%93%E6%9E%9C-1)
        - [TPS略有下降的原因分析](#tps%E7%95%A5%E6%9C%89%E4%B8%8B%E9%99%8D%E7%9A%84%E5%8E%9F%E5%9B%A0%E5%88%86%E6%9E%90)
        - [FGC次数增多的原因分析](#fgc%E6%AC%A1%E6%95%B0%E5%A2%9E%E5%A4%9A%E7%9A%84%E5%8E%9F%E5%9B%A0%E5%88%86%E6%9E%90)
    - [执行方式](#%E6%89%A7%E8%A1%8C%E6%96%B9%E5%BC%8F-1)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## 👻 内存泄漏

对比测试[`TransmittableThreadLocal`](../src/main/java/com/alibaba/ttl/TransmittableThreadLocal.java)和[`ThreadLocal`](https://docs.oracle.com/javase/10/docs/api/java/lang/ThreadLocal.html)，测试Case是：

简单一个线程一直循环`new` `TransmittableThreadLocal`、`ThreadLocal`实例，不主动做任何清理操作，即不调用`ThreadLocal`的`remove`方法主动清空。

### 验证结果

都可以持续运行，不会出内存溢出`OutOfMemoryError`。

### 执行方式

可以通过执行工程下的脚本来运行Case验证：

* 脚本[`memoryleak-ThreadLocal.sh`](../scripts/perf-test/memoryleak-ThreadLocal.sh)运行`ThreadLocal`的测试。  
测试类是[`NoMemoryLeak_ThreadLocal_NoRemove`](../src/test/java/com/alibaba/perf/memoryleak/NoMemoryLeak_ThreadLocal_NoRemove.kt)。
* 脚本[`memoryleak-TransmittableThreadLocal.sh`](../scripts/perf-test/memoryleak-TransmittableThreadLocal.sh)运行`TransmittableThreadLocal`的测试。
测试类是[`NoMemoryLeak_TransmittableThreadLocal_NoRemove`](../src/test/java/com/alibaba/perf/memoryleak/NoMemoryLeak_TransmittableThreadLocal_NoRemove.kt)。

## 🐎 TPS & 压力测试

对比测试[`TransmittableThreadLocal`](../src/main/java/com/alibaba/ttl/TransmittableThreadLocal.java)和[`ThreadLocal`](https://docs.oracle.com/javase/10/docs/api/java/lang/ThreadLocal.html)，测试Case是：

2个线程并发一直循环`new` `TransmittableThreadLocal`、`ThreadLocal`实例，不主动做任何清理操作，即不调用`ThreadLocal`的`remove`方法主动清空。

### 验证结果

在我的4核开发机上运行了24小时，稳定正常。

TPS结果如下：

`ThreadLocal`的TPS稳定在～41K：

```bash
......
tps: 42470
tps: 40940
tps: 41041
tps: 40408
tps: 40610
```

`TransmittableThreadLocal`的TPS稳定在～40K：

```bash
......
tps: 40461 
tps: 40101 
tps: 39989 
tps: 40684 
tps: 41174 
```

GC情况如下（1分钟输出一次）：

`ThreadLocal`的每分钟GC时间是`5.45s`，FGC次数是`0.09`：

```bash
   S0     S1      E      O      P    YGC      YGCT     FGC     FGCT   GCT
......
  0.00  97.66   0.00   8.33  12.70 1470935 2636.215    41    0.229 2636.444
 97.66   0.00   0.00  17.18  12.70 1473968 2640.597    41    0.229 2640.825
 98.44   0.00   0.00  25.47  12.70 1477020 2645.265    41    0.229 2645.493
 96.88   0.00  33.04  34.03  12.70 1480068 2650.149    41    0.229 2650.378
  0.00  97.66  14.01  41.82  12.70 1483113 2655.262    41    0.229 2655.490
  0.00  97.66  74.07  50.25  12.70 1486149 2660.596    41    0.229 2660.825
 96.88   0.00   0.00  58.32  12.70 1489170 2666.135    41    0.229 2666.364
 98.44   0.00  26.07  67.05  12.70 1492162 2671.841    41    0.229 2672.070
  0.00  97.66   0.00  76.50  12.70 1495139 2677.809    41    0.229 2678.038
  0.00  97.66   0.00  85.95  12.70 1498091 2683.994    41    0.229 2684.222
 96.88   0.00   0.00  96.50  12.70 1501038 2690.454    41    0.229 2690.683
 97.66   0.00   0.00   7.96  12.70 1504054 2695.583    42    0.233 2695.816
  0.00  97.66   0.00  17.46  12.70 1507099 2700.009    42    0.233 2700.241
  0.00  97.66   0.00  26.97  12.70 1510133 2704.652    42    0.233 2704.885
 97.66   0.00   0.00  36.57  12.70 1513158 2709.592    42    0.233 2709.825
  0.00  97.66   0.00  45.59  12.70 1516167 2714.738    42    0.233 2714.971
 98.44   0.00   0.00  54.49  12.70 1519166 2720.109    42    0.233 2720.342
  0.00  98.44   0.00  63.52  12.70 1522141 2725.688    42    0.233 2725.921
  0.00  97.66  84.18  72.00  12.70 1525139 2731.579    42    0.233 2731.812
  0.00  98.44  20.04  80.10  12.70 1528121 2737.680    42    0.233 2737.913
  0.00  97.66  28.06  87.70  12.70 1531093 2743.991    42    0.233 2744.224
  0.00  98.44   0.00  95.63  12.70 1534055 2750.508    42    0.233 2750.741
 97.66   0.00   0.00   4.75  12.70 1537062 2756.196    43    0.239 2756.435
```

`TransmittableThreadLocal`的每分钟GC时间是`5.29s`，FGC次数是`3.27`：

```bash
   S0     S1      E      O      P    YGC      YGCT     FGC     FGCT   GCT
......
  0.00  98.44   8.01  57.38  12.80 1390879 2571.496  1572    9.820 2581.315
  0.00  97.66   0.00  78.87  12.80 1393725 2576.784  1575    9.839 2586.623
 98.44   0.00  14.04   5.83  12.80 1396559 2582.082  1579    9.866 2591.948
 98.44   0.00   0.00  26.41  12.80 1399394 2587.274  1582    9.885 2597.159
 98.44  98.44   0.00  50.75  12.80 1402230 2592.506  1585    9.904 2602.410
 98.44   0.00   0.00  84.37  12.80 1405077 2597.808  1588    9.925 2607.733
  0.00  98.44   0.00   5.19  12.80 1407926 2603.108  1592    9.952 2613.059
  0.00  98.44  58.17  29.80  12.80 1410770 2608.314  1595    9.973 2618.287
 99.22   0.00   0.00  54.14  12.80 1413606 2613.582  1598    9.992 2623.574
 98.44   0.00   0.00  78.18  12.80 1416444 2618.881  1601   10.012 2628.893
  0.00  97.66   0.00   7.36  12.80 1419275 2624.167  1605   10.038 2634.205
  0.00  99.22   0.00  31.04  12.80 1422125 2629.391  1608   10.057 2639.448
  0.00  98.44   0.00  60.41  12.80 1424974 2634.636  1611   10.077 2644.714
  0.00  98.44   0.00  84.72  12.80 1427825 2639.929  1614   10.094 2650.024
  0.00  97.66   0.00  12.32  12.80 1430679 2645.204  1618   10.119 2655.323
  0.00  98.44  12.05  39.31  12.80 1433539 2650.442  1621   10.141 2660.583
 86.81   0.00   0.00  67.40  12.80 1436392 2655.743  1624   10.156 2665.899
 99.22   0.00   0.00  95.25  12.80 1439244 2661.071  1627   10.175 2671.246
 98.44   0.00   0.00  24.63  12.80 1442090 2666.305  1631   10.201 2676.506
  0.00  99.22   0.00  52.86  12.80 1444945 2671.546  1634   10.222 2681.769
 98.44   0.00   0.00  80.38  12.80 1447802 2676.850  1637   10.241 2687.091
  0.00  87.50   0.00   4.22  12.80 1450658 2682.196  1641   10.268 2692.464
 99.22   0.00   0.00  33.22  12.80 1453507 2687.386  1644   10.290 2697.676
```

#### TPS略有下降的原因分析

使用`jvisualvm` Profile方法耗时，`TransmittableThreadLocal`Case的热点方法和`ThreadLocal`Case一样。

略有下降可以认为是Full GC更多引起。

实际使用场景中，`TransmittableThreadLocal`实例个数非常有限，不会有性能问题。

#### FGC次数增多的原因分析

在`TransmittableThreadLocal.holder`中，持有`TransmittableThreadLocal`实例的弱引用，减慢实例的回收，导致Full GC增加。

实际使用场景中，`TransmittableThreadLocal`实例个数非常有限，不会有性能问题。

### 执行方式

可以通过执行工程下的脚本来运行Case验证：

* 脚本[`tps-ThreadLocal.sh`](../scripts/perf-test/tps-ThreadLocal.sh)运行`ThreadLocal`的测试。  
测试类是[`CreateThreadLocalInstanceTps`](../src/test/java/com/alibaba/perf/tps/CreateThreadLocalInstanceTps.kt)。
* [`tps-TransmittableThreadLocal.sh`](../scripts/perf-test/tps-TransmittableThreadLocal.sh)运行`TransmittableThreadLocal`的测试。
测试类是[`CreateTransmittableThreadLocalInstanceTps`](../src/test/java/com/alibaba/perf/tps/CreateTransmittableThreadLocalInstanceTps.kt)。
