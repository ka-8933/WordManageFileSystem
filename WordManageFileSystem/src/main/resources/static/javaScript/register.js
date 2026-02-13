
//DOC获取集
var usernameDoc = document.getElementById('username');
var passwordDoc = document.getElementById('password');

//注册处理
function registerHander(){
    if (usernameDoc.value === "" || passwordDoc.value === ""){
        alert("用户名和密码不能为空!!");
        return;
    }

    const registerBody = {
        username : usernameDoc.value,
        password : passwordDoc.value
    }
    axios.post('http://localhost:8080/register/insert' , registerBody)
        .then(response => {
            const result = response.data;
            //获得注册状态
            const registerCondition = result.data.registerCondition;
            //获得用户名和密码
            const username = result.data.username;
            const password = result.data.password;
            alert(`${registerCondition}` + "\n" + `${username}` + "\n" + `${password}`);
            clearInput();
            window.location.href = "../html/login.html";
        });
}

function clearInput(){
    usernameDoc.value = "";
    passwordDoc.value = "";
}