<script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=default"></script>

# Logistic Regression
## logistic回归分析

### sigmoid函数
$$f(z)=1/(1+e^{-z})$$

### sigmoid函数求导过程
$$除法求导公式->f(x)/g(x)=[f'(x)g(x)-f(x)g'(x)]/[g(x)]^2$$
$$单独的(e^{-z})'求导$$
$$(e^{-z})'=1/(e^z)=[f'(1)g(e^z)-f(1)g'(e^z)]/[g(e^z)]^2$$
                 $$=[0-e^z]/(e^z)^2$$
                 $$=-e^z/e^{2z}$$
                 $$=-e^{-x}$$

---

$$f'(z)=[f'(1)g(1+e^{-z})-f(1)g'(1+e^{-z})]/[g(1+e^{-z})]^2$$
         $$=[0-(1*(-e^{-x}))]/[g(1+e^{-z})]^2$$
         $$=e^{-x}/[g(1+e^{-z})]^2$$
         $$=1+e^{-x-1}/(1+e^{-z})^2$$
         $$=(1+e^{-x}/(1+e^{-z})^2)-(1/(1+e^{-z})^2)$$
         $$=(1/(1+e^{-z}))-(1/(1+e^{-z})^2)$$
         $$=1/(1+e^{-z})[1-(1/(1+e^{-z}))]$$
         $$=f(x)[1-f(x)]=y(1-y)$$


### 梯度上升法
$$∇f(x,y)={∂f(x,y)/∂x,∂f(x,y)/∂y}$$
$$w=w+a∇f(w)$$