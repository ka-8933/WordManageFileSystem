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
function showMiniToast(content, duration = 4000) {
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

// ========== 功能型弹窗（showMessage）- 优化成简洁风格 ==========
function showMessage(text, type = 'info') {
    injectToastStyle(); // 复用样式注入函数，避免重复
    // 移除旧弹窗
    const oldMsg = document.querySelector('.msg-toast');
    if (oldMsg) oldMsg.remove();
    // 创建新弹窗
    const msgDom = document.createElement('div');
    msgDom.className = `msg-toast ${type}`;
    msgDom.innerText = text;
    document.body.appendChild(msgDom);
    // 自动消失（动画+移除）
    setTimeout(() => {
        msgDom.style.opacity = '0';
        msgDom.style.transform = 'translateX(-50%) scale(0.98)';
        setTimeout(() => msgDom.remove(), 250);
    }, 3000);
}
window.showMessage = showMessage;



//退出按钮
function Logout() {
    localStorage.setItem("token", null);
    window.location.href = `login.html`;
}

//DOC参数集合
var nameDoc = document.getElementById('name'); //昵称
var phoneNumberDoc = document.getElementById('phoneNumber'); //电话
var birthDayDoc = document.getElementById('birthDay'); //生日
var imgDoc = document.getElementById('img'); //生日
var gradeDoc = document.getElementById('grade'); //学历
var genderDoc = document.getElementById('gender'); //性别
var cityDoc = document.getElementById('city'); //介绍

//头像展示栏
var imgShowDoc = document.getElementById('imgShow');

//昵称展示栏
var nameShowDoc = document.getElementById('nameShow');

//保存用户基础信息到后端
async function postMagApi() {
    //传入数据json
    const msgPostJson = {
        name: nameDoc.value,
        phoneNumber: phoneNumberDoc.value,
        birthDay: birthDayDoc.value,
        grade: gradeDoc.value,
        gender: genderDoc.value,
        city: cityDoc.value
    };

    //先传头像给后端

    //封装文件头像
    const file = imgDoc.files[0];
    const formData = new FormData();
    formData.append('file' , file);

    //判断头像和基础数据是否都为空
    if (!file && !msgPostJson){
        showMessage("请输入基本信息！！");
        return;
    }

    //判断文件是否为空
    if (!file) { //文件是空

        //再上传用户信息
        const token2 = localStorage.getItem("token"); //得到token
        const response = await axios.put('http://localhost:8080/msg/msg' ,
            msgPostJson ,
            {headers : {userToken : token2}});

        const resultMsg = response.data;
        const dataMsg = resultMsg.code;
        if (parseInt(dataMsg) === 200){
            showMessage("用户信息上传成功！！")
        }else{
            showMessage("用户信息上传失败！！")
        }

        getBaseMsgToInput();
    }else if (file && !msgPostJson){ //如果只是头像不为空

        //插入axios
        const tokenImg = localStorage.getItem("token");
        const responseImg = await axios.put('http://localhost:8080/msg/img' , formData ,
            {headers : {userToken : tokenImg}});
        const resultImg = responseImg.data;
        const storResult = resultImg.code;
        //判断是否成功
        if (parseInt(storResult) === 1){
            showMessage("头像上传成功！！")
        }else {
            showMessage("头像上传失败！！")
        }

        getBaseMsgToInput();
    }else if (file && msgPostJson){ //两者都不为空

        const formData = new FormData();
        formData.append('file' , file);

        //插入axios
        const tokenImg = localStorage.getItem("token");
        const responseImg = await axios.put('http://localhost:8080/msg/img' , formData ,
            {headers : {userToken : tokenImg}});
        const resultImg = responseImg.data;
        const storResult = resultImg.code;
        //判断是否成功
        if (parseInt(storResult) === 1){
            showMessage("头像上传成功！！")
        }else {
            showMessage("头像上传失败！！")
        }

        //再上传用户信息
        const token2 = localStorage.getItem("token"); //得到token
        const response = await axios.put('http://localhost:8080/msg/msg' ,
            msgPostJson ,
            {headers : {userToken : token2}});

        const resultMsg = response.data;
        const dataMsg = resultMsg.code;
        if (parseInt(dataMsg) === 200){
            showMessage("用户信息上传成功！！")
        }else{
            showMessage("用户信息上传失败！！")
        }

        getBaseMsgToInput();
    }else{
        showMessage("逻辑漏洞！")
    }
}

/*
* {
    "code": 200,
    "msg": "已查询或已成功！",
    "hasSearchTotal": null,
    "totalPage": null,
    "currentPage": null,
    "data": {
        "userId": 1,
        "name": "小卡爱吃糖",
        "phoneNumber": "19374588933",
        "birthDay": "2005-06-01",
        "img": "https://java-web-xiaoka.oss-cn-beijing.aliyuncs.com/uploads/1771072697392.png",
        "grade": "曼波",
        "gender": "男",
        "city": "北京"
    },
    "right": false
}*/

/*
* nameDoc = document.getElem
phoneNumberDoc = document.
birthDayDoc = document.get
imgDoc = document.getEleme
gradeDoc = document.getEle
genderDoc = document.getEl
cityDoc = document.getElem*/

//将用户数据渲染到input里面
async function getBaseMsgToInput() {
    const token = localStorage.getItem("token"); //得到token
    const response = await axios.get('http://localhost:8080/msg',
        {headers: {userToken: token}});
    //获取用户基本数据
    const result = response.data;
    const data = result.data;
    //将用户数据渲染给input
    nameDoc.value = data.name || '';
    phoneNumberDoc.value = data.phoneNumber || '';
    birthDayDoc.value = data.birthDay;
    gradeDoc.value = data.grade || '';
    genderDoc.value = data.gender;
    cityDoc.value = data.city || '';

    //将图片单独渲染给头像栏
    imgShowDoc.innerHTML = `<img src="${data.img}" alt="">`

    //将名字单独渲染给名字栏
    nameShowDoc.innerHTML = data.name || '';
}

//开局自动加载
document.addEventListener('DOMContentLoaded' , function (){
    getBaseMsgToInput();
})

