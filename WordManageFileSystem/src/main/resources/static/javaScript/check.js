let totalNum = 0;
let rightNum = 0;
let currentWord = '';

var totalNumDoc = document.getElementById('totalNum');
var rightNumDoc = document.getElementById('rightNum');
var wordDisplayDoc = document.getElementById('wordDisplay');
var inputMeaningDoc = document.getElementById('inputMeaning');

var wordDisplayDoc = document.getElementById('wordDisplay');

//点击开始按钮
function start() {
    axios.get('http://localhost:8080/word/random')
        .then(response => {
            const result = response.data;
            const getWord = result.data;
            wordDisplayDoc.innerHTML = getWord.word;
            totalNum = totalNum + 1;
            totalNumDoc.innerHTML = totalNum;
            currentWord = getWord.word;
        });
}

function skip(){
    alert("确定跳过？")
    axios.get('http://localhost:8080/word/random')
        .then(response => {
            const result = response.data;
            const getWord = result.data;
            wordDisplayDoc.innerHTML = getWord.word;
        });
}

function end(){
    alert("抽查结束 ，一共抽查："+totalNum+"次 ， 正确数为："+rightNum+"个");
    totalNum = 0;
    rightNum = 0;
    totalNumDoc.innerHTML = totalNum;
    rightNumDoc.innerHTML = rightNum;
    wordDisplayDoc.innerHTML = "点击开始抽查单词";
}

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

function submiton(){
    const checkResponseJson = {
        word:currentWord,
        inputMeaning:inputMeaningDoc.value,
    }
    axios.put('http://localhost:8080/word/updateAccuracy' , checkResponseJson ,{headers : {userToken : token}})
        .then(response => {
            const result = response.data;
            const word = result.data;
            if (result.right === true){
                rightNum++;
                rightNumDoc.innerHTML = rightNum;
                showMessage(word.word+'输入正确！！ 单词释义为：'+rightNum, 'success');
                currentWord = '';
                end()
                start();
                inputMeaningDoc.value = '';
                inputMeaningDoc.focus();
            }else{
                showMessage(word.word+'输入错误！！ 单词释义为：'+word.meaning, 'error');
                currentWord = '';
                end()
                start();
                inputMeaningDoc.value = '';
                inputMeaningDoc.focus();
            }
        });
}
//