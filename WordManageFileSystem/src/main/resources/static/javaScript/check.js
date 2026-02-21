// ========== 核心弹窗：极简粉色款（showMiniToast） ==========
function injectToastStyle() {
    if (document.getElementById('mini-toast-style')) return;
    const style = document.createElement('style');
    style.id = 'mini-toast-style';
    style.innerHTML = `
        /* 极简粉色弹窗 - 更精致的简洁风格 */
        .mini-toast {
            position: fixed;
            top: 20px;
            left: 50%;
            transform: translateX(-50%);
            padding: 10px 22px; /* 精简内边距，更小巧 */
            background: #fff;
            border: 1px solid #fce4ec; /* 更浅的粉色边框，更柔和 */
            border-radius: 6px; /* 更精致的小圆角 */
            box-shadow: 0 1px 6px rgba(255, 105, 180, 0.15); /* 更轻的阴影，不抢眼 */
            z-index: 9999;
            font-size: 14px;
            color: #333;
            text-align: center;
            font-family: -apple-system, BlinkMacSystemFont, "Microsoft YaHei", sans-serif;
            /* 更丝滑的动画 */
            animation: slideIn 0.25s ease-out forwards, fadeOut 0.25s ease-in 3.75s forwards;
        }
        /* 粉色小标识 - 更细更精致 */
        .mini-toast::before {
            content: '';
            position: absolute;
            left: 0;
            top: 0;
            height: 100%;
            width: 2px; /* 缩窄竖线，更简洁 */
            background: #ff80ab; /* 更温柔的粉色，不刺眼 */
            border-radius: 6px 0 0 6px;
        }
        /* 滑入动画 - 更自然 */
        @keyframes slideIn {
            0% { top: -30px; opacity: 0; transform: translateX(-50%) scale(0.98); }
            100% { top: 20px; opacity: 1; transform: translateX(-50%) scale(1); }
        }
        /* 渐隐动画 - 更丝滑 */
        @keyframes fadeOut {
            0% { opacity: 1; transform: translateX(-50%) scale(1); }
            100% { opacity: 0; transform: translateX(-50%) scale(0.98); }
        }

        /* ========== 功能型弹窗（showMessage）也优化成简洁风格 ========== */
        .msg-toast {
            position: fixed;
            top: 20px;
            left: 50%;
            transform: translateX(-50%);
            padding: 9px 20px;
            border-radius: 6px;
            color: #fff;
            z-index: 9999;
            font-size: 14px;
            text-align: center;
            font-family: -apple-system, BlinkMacSystemFont, "Microsoft YaHei", sans-serif;
            box-shadow: 0 1px 6px rgba(0, 0, 0, 0.1);
            transition: all 0.25s ease;
            opacity: 1;
        }
        /* 不同类型的颜色 - 更柔和的色调 */
        .msg-toast.success { background: #7ccc62; }
        .msg-toast.warning { background: #f0b95e; }
        .msg-toast.error { background: #f87272; }
        .msg-toast.info { background: #64b5f6; }
    `;
    document.head.appendChild(style);
}

// 极简粉色弹窗函数（保留核心逻辑，样式优化）
function showMiniToast(content, duration = 3000) {
    injectToastStyle();
    // 移除旧弹窗，避免叠加
    const oldToast = document.querySelector('.mini-toast');
    if (oldToast) oldToast.remove();
    // 创建新弹窗
    const toast = document.createElement('div');
    toast.className = 'mini-toast';
    toast.innerHTML = content;
    document.body.appendChild(toast);
    // 自动消失
    setTimeout(() => {
        toast.remove();
    }, duration);
}

window.showMiniToast = showMiniToast;


//DOC参数
var timeSelectDoc = document.getElementById('timeSelect');
var currentWordDoc = document.getElementById('currentWord');
var userAnswerDoc = document.getElementById('userAnswer');
var historyListDoc = document.getElementById('historyList');


//全局单词数组
let wordArr = null;
let curr = null;
let index = 0;

//开始抽查
async function startCheck() {
    const res = await axios.get('http://localhost:8080/check',
        {params: {pageSize: parseInt(timeSelectDoc.value)}})
    const result = res.data;
    wordArr = result.data; //
    console.log(wordArr);
    showMiniToast("开始抽查，本次抽查次数为：" + parseInt(timeSelectDoc.value) + "次");
    currentWordPrint();
}

function currentWordPrint(){
    curr = wordArr[index];
    currentWordDoc.innerHTML = curr.word;
}

function shut(){
    curr = null;
    index = 0;
    currentWordDoc.innerHTML = "请点击开始";
    showMiniToast("已经结束本次抽查！！");
    historyListDoc.innerHTML = ``;
}

async function submitAnswer() {
    if (index >= parseInt(timeSelectDoc.value)) {
        index = 0;
        return;
    }
    submitionBody = {
        word : wordArr[index].word,
        inputMeaning : String(userAnswerDoc.value)
    };

    const token = localStorage.getItem("token")
    const res = await axios.put('http://localhost:8080/word/updateAccuracy',
        submitionBody , {headers : {userToken : token}});

    const result = res.data;
    const isRight = result.right;
    console.log(isRight);
    clearInput();
    if (isRight === true){
        showMiniToast("回答正确！！");
        var row = ` <div class='history-item' >
            ${wordArr[index].word} —— <span style='color:green'>${userAnswerDoc.value}</span>（正确：${wordArr[index].meaning}）
            </div>`;
        historyListDoc.innerHTML += row;

    }else{
        showMiniToast("回答错误！！");
        var row = ` <div class='history-item' > 
            ${wordArr[index].word} —— <span style='color:red'>${userAnswerDoc.value}</span>（正确：${wordArr[index].meaning}）
            </div>`;
        historyListDoc.innerHTML += row;
    }

    index++;
    currentWordPrint();
}

function clearInput(){
    userAnswerDoc.value = "";
    userAnswerDoc.focus();
}


