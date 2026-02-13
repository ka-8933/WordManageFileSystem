// 原生自定义提示框（无外部依赖，不影响布局）
function showMessage(text, type = 'info') {
    const msgDom = document.createElement('div');
    msgDom.style.cssText = `
                position: fixed;
                top: 20px;
                left: 50%;
                transform: translateX(-50%);
                padding: 10px 20px;
                border-radius: 4px;
                color: #fff;
                z-index: 9999;
                font-size: 14px;
                transition: opacity 0.3s;
                box-shadow: 0 2px 12px rgba(0,0,0,0.1);
            `;
    // 提示框颜色（成功/警告/错误）
    switch(type) {
        case 'success': msgDom.style.backgroundColor = '#67c23a'; break;
        case 'warning': msgDom.style.backgroundColor = '#e6a23c'; break;
        case 'error': msgDom.style.backgroundColor = '#f56c6c'; break;
        default: msgDom.style.backgroundColor = '#409eff';
    }
    msgDom.innerText = text;
    document.body.appendChild(msgDom);

    // 3秒后自动消失
    setTimeout(() => {
        msgDom.style.opacity = '0';
        setTimeout(() => msgDom.remove(), 300);
    }, 3000);
}

// 获取DOM元素
const loginBtn = document.getElementById('loginBtn');
const usernameInput = document.getElementById('username');
const passwordInput = document.getElementById('password');

// 登录核心逻辑（适配后端响应）
loginBtn.addEventListener('click', async function() {
    // 1. 表单校验
    const username = usernameInput.value.trim();
    const password = passwordInput.value.trim();

    if (!username) {
        showMessage('请输入用户名', 'warning');
        usernameInput.focus();
        return;
    }
    if (!password) {
        showMessage('请输入密码', 'warning');
        passwordInput.focus();
        return;
    }

    // 2. 防止重复提交
    loginBtn.disabled = true;
    loginBtn.innerText = '登录中...';

    try {
        // 3. 发送登录请求（===== 替换为你的实际后端接口地址 =====）
        const response = await fetch('http://localhost:8080/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json', // 与后端协商的提交格式
            },
            body: JSON.stringify({ username, password }), // 提交用户名和密码
            credentials: 'include' // 跨域时需携带Cookie（如后端需要）
        });

        // 4. 解析后端响应（完全匹配你提供的格式）
        const result = await response.json();

        // 5. 处理响应结果
        // 登录成功后，修改你的代码：
        if (response.ok && result.code === 200) {
            console.log("=== 登录调试 ===");

            // 1. 确认取到了
            const userId = result.data.id;
            console.log("取到的userId:", userId); // 应该是 1

            // 2. 存储token
            const token = result.data.token;
            localStorage.setItem('token', token);

            // 3. 检查localStorage所有内容
            console.log("localStorage所有内容:");
            for(let i = 0; i < localStorage.length; i++) {
                const key = localStorage.key(i);
                console.log(`${key}: ${localStorage.getItem(key)}`);
            }

            // 4. 用URL参数传递（最保险）
            showMessage(`${result.msg}，欢迎 ${result.data.username}!`, 'success');

            setTimeout(() => {
                // ✅ 关键：用URL参数传递
                window.location.href = `home.html`;
            }, 1000);
        }else {
            // 登录失败（后端返回非200状态）
            showMessage(result.msg || '登录失败，请检查账号密码', 'error');
        }
    } catch (error) {
        // 处理网络异常（如接口不可用、跨域问题）
        console.error('登录请求异常：', error);
        showMessage('网络错误，请稍后重试', 'error');
    } finally {
        // 恢复按钮状态
        loginBtn.disabled = false;
        loginBtn.innerText = '登录';
    }
});

// 回车触发登录（优化用户体验）
[usernameInput, passwordInput].forEach(input => {
    input.addEventListener('keyup', (e) => {
        if (e.key === 'Enter') loginBtn.click();
    });
});

// 第三方登录逻辑（占位，需后端配合）
document.querySelectorAll('.thirdLogin').forEach(btn => {
    btn.addEventListener('click', function() {
        const type = this.dataset.type;
        const typeName = type === 'wechat' ? '微信' : type === 'qq' ? 'QQ' : 'GitHub';
        showMessage(`正在跳转到${typeName}授权页...`);

        // ===== 替换为你的第三方登录授权接口 =====
        setTimeout(() => {
            window.location.href = `https://api.xiaoka.com/third-login/${type}`;
        }, 1000);
    });
});