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

//保存用户基础信息到后端
async function postMagApi() {
    msgPostJson = {
        name: nameDoc.value,
        phoneNumber: phoneNumberDoc.value,
        birthDay: birthDayDoc.value,
        img : imgDoc.value,
        grade: gradeDoc.value,
        gender: genderDoc.value,
        city: cityDoc.value
    };
    const token = localStorage.getItem("token"); //得到token
    const response = await axios.post('http://localhost:8080/msg' ,
        {msgPostJson : msgPostJson} ,
        {headers : {token : token}}
    );
    const data = response.data;
    const code = data.code;
    if (code === 200){
        //...
        getBaseMsgToInput();
    }
}

//将用户数据渲染到input里面
async function getBaseMsgToInput() {
    const token = localStorage.getItem("token"); //得到token
    const response = await axios.get('http://localhost:8080/msg',
        {headers: {token: token}}
    );
    const data = response.data;

}

//开局自动加载
document.addEventListener('DOMContentLoaded' , function (){
    getBaseMsgToInput();
})

