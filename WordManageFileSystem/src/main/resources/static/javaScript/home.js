//保留一共多少页
let saveTotalPage = 1;

//当前页数
let currentPage = 1;

//当前按钮状态
let currentChecked = 1;

//滑块按钮
const switchDom = document.getElementById('savePageSwitch');
const savePromptDoc = document.getElementById('savePrompt');

var getSideSearch = document.getElementById('sideButton');
var getSizeInput = document.getElementById('search');
getSideSearch.addEventListener('click', () => {
    var getMsg = getSizeInput.value.trim();
    alert("别找了！找不到" + getSideSearch)
    getSizeInput.value = "";
    return;
});

// 时间设置
function getTime() {
    var getDate = new Date();
    var h = getDate.getHours();
    var m = getDate.getMinutes();
    var s = getDate.getSeconds();
    var getM = addZero(m);
    var getS = addZero(s);
    document.getElementById('time').innerHTML = h + ":" + getM + ":" + getS;
}

setInterval(getTime, 100);

function addZero(time) {
    if (time < 10) {
        var time = "0" + time;
        return time;
    } else {
        return time;
    }
}

//清除查询input数据
var getSelectDegrad = document.getElementById('selectDegrad');
var getWordClass = document.getElementById('wordClass');
var getMeaning = document.getElementById('meaning');
var getWord = document.getElementById('word');
document.getElementById('clear').addEventListener('click', () => {
    getSelectDegrad.value = "";
    getWordClass.value = "";
    getMeaning.value = "";
    getWord.value = "";
});

/**
 * 滑块按钮设置
 */

// ========== 核心优化：统一管理开关状态，避免重复请求 ==========
// 1. 初始化开关状态（页面加载时只请求一次）
async function initSwitchStatus() {
    try {
        const token = localStorage.getItem("token");

        // 只请求一次后端获取初始状态
        const response = await axios.get('http://localhost:8080/setting', {
            headers: {userToken: token}
        });
        const result = response.data;
        const data = result.data || {};
        const isSavePage = data.isSavePage;

        //初始化将数据给全局
        //初始化将数据给全局
        const page = result.savePage; // 页面input初始化
        currentPage = parseInt(page); //全局当前页数初始化

        // 同步开关状态和提示文案
        switchDom.checked = !!isSavePage; // 强制转布尔值，避免NaN/null
        updatePrompt();
    } catch (error) {
        console.error('初始化开关状态失败：', error);
        switchDom.checked = false;
        updatePrompt();
    }
}

// 2. 更新提示文案（保留原有逻辑）
function updatePrompt() {
    const promptText = switchDom.checked ? '保留页码已启动' : '保留页码未启动';
    savePromptDoc.innerHTML = `<h5>${promptText}</h5>`;
}

// 3. 开关变更事件（核心：用前端当前状态，不重复请求后端）
switchDom.addEventListener('change', async () => {
    updatePrompt(); // 先更新前端提示，提升体验

    try {
        // 1. 获取并校验Token
        const token = localStorage.getItem("token");
        if (!token) {
            switchDom.checked = !switchDom.checked; // 恢复状态
            return;
        }

        // 2. 直接用前端当前的开关状态（无需请求后端，避免NaN）
        const currentChecked = switchDom.checked;
        const switchDomResult = Number(currentChecked); // true→1，false→0
        const savePageResult = parseInt(currentPage) || 1; // 兜底：确保页码有效

        // 3. 构造请求体
        const settingBody = {
            isSavePage: switchDomResult,
            savePage: savePageResult
        };

        // 4. 发送更新请求（异步/await 更易读）
        const response = await axios.put('http://localhost:8080/setting', settingBody, {
            headers: {userToken: token}
        });
        const result = response.data;
        const data = result.data || {};

        // 5. 确认状态（优先用前端操作的状态，避免后端返回异常值）
        switchDom.checked = currentChecked;

    } catch (error) {
        // 失败时恢复开关状态，确保可重复点击
        switchDom.checked = !switchDom.checked;
        console.error('保存设置失败：', error);
    }
});

// 页面加载时初始化开关状态
initSwitchStatus();


//一共x条数据
var totalPageDoc = document.getElementById('totalPage');
//input页码


//分页查询单词
//通过“查询全部“按钮，点击会根据当前的页数和页数规模
//显示第n页的所有数据。
var queryAllWordDoc = document.getElementById('queryAllWord');

queryAllWordDoc.addEventListener('click', async () => {

    const page = document.getElementById('page'); //此时 page.value = 数据库储存的值
    const pageDoc = page.value;
    const pageSize = document.getElementById('pageSizeSelect');
    const pageSizeDoc = pageSize.value;
    // 渲染实时页数
    var currentPageDoc = document.getElementById('currentPage');
    currentPageDoc.innerHTML = pageDoc;

    const token = localStorage.getItem('token')

    //查询
    const response = await axios.get('http://localhost:8080/word/page', {
            headers: {userToken: token},
            params: {page: parseInt(pageDoc) || 1, pageSize: pageSizeDoc}
        }
    );

    const getResult = response.data;
    const words = getResult.data;
    const hasSearchTotal = getResult.hasSearchTotal;
    const totalPage = getResult.totalPage; //一共页数
    const currentPageData = getResult.currentPage; //得到保存的当前页码

    page.value = parseInt(currentPageData , 10);

    totalPageDoc.innerHTML = totalPage; //一共页数赋值
    saveTotalPage = totalPage;

    currentPage = parseInt(currentPageData, 10);

    //将input的值改为当前页面
    var currentPageValue = parseInt(currentPageData, 10) || 1;
    page.value = currentPageValue;

    //先将查询的总数渲染在页面
    var getHasSearchTotalDoc = document.getElementById('hasSearchTotal');
    getHasSearchTotalDoc.innerHTML = hasSearchTotal;

    //这里将数据转化成table中的tbody，使用函数
    createWordTbody(words);
    //判断保存按钮是否要保存页码

    if (switchDom.checked === true) {
        // 1. 获取并校验Token
        const token = localStorage.getItem("token");

        // 2. 直接用前端当前的开关状态（无需请求后端，避免NaN）
        const currentChecked = switchDom.checked;
        const switchDomResult = Number(currentChecked); // true→1，false→0
        const savePageResult = parseInt(currentPage) || 1; // 兜底：确保页码有效

        // 3. 构造请求体
        const settingBody = {
            isSavePage: switchDomResult,
            savePage: savePageResult
        };

        // 4. 发送更新请求（异步/await 更易读）
        const response = await axios.put('http://localhost:8080/setting', settingBody, {
            headers: {userToken: token}
        });
        const result = response.data;
        const data = result.data || {};

        // 5. 确认状态（优先用前端操作的状态，避免后端返回异常值）
        switchDom.checked = currentChecked;

        // 渲染实时页数
        var currentPageDoc = document.getElementById('currentPage');
        currentPageDoc.innerHTML = currentPage;
    }
});

function createWordTbody(words) {
    //获取所有数据
    const tbody = document.querySelector('#wordTable tbody');
    tbody.innerHTML = '';

    //将数据逐行用tr为单位渲染出来
    words.forEach(element => {
        //根据准确率判断不同样式的tr
        if (element.accuracy >= 90 && element.total != null) {
            const row = document.createElement('tr');
            row.innerHTML = `
                            <th><input type="checkbox"></th>
                            <th><h4>${element.id}</h4></th>
                            <th><h4 style="color: rgb(96, 39, 129);font-size: 12.5px"><i>${element.word}</i></h4></th>
                            <th><h4 style="color: rgb(59, 59, 59);">${element.meaning}</h4></th>
                            <th><h4>${element.partOfSpeech}</h4></th>
                            <th><h4>${element.belongGrade}</h4></th>
                            <th><h4>${element.similarWord}</h4></th>
                            <th><h4>${element.phrase}</h4></th>
                            <th><div class="passPercent_Best"><h4>${element.accuracy}%</h4></div></th>
                            <th>
                                <span class="delete_span"><button onclick="deletePublicWord(${element.id})">删除</button></span>
                                <span class="revise_span"><button onclick="openUpdateBar(${element.id})">修改</button></span>
                            </th>
                        `;
            tbody.appendChild(row);
        } else if (element.accuracy >= 80 && element.accuracy < 90 && element.total != null) {
            const row = document.createElement('tr');
            row.innerHTML = `
                            <th><input type="checkbox"></th>
                            <th><h4>${element.id}</h4></th>
                            <th><h4 style="color: rgb(96, 39, 129);font-size: 12.5px"><i>${element.word}</i></h4></th>
                            <th><h4 style="color: rgb(59, 59, 59);">${element.meaning}</h4></th>
                            <th><h4>${element.partOfSpeech}</h4></th>
                            <th><h4>${element.belongGrade}</h4></th>
                            <th><h4>${element.similarWord}</h4></th>
                            <th><h4>${element.phrase}</h4></th>
                            <th><div class="passPercent_hight"><h4>${element.accuracy}%</h4></div></th>
                            <th>
                                <span class="delete_span"><button onclick="deletePublicWord(${element.id})">删除</button></span>
                                <span class="revise_span"><button onclick="openUpdateBar(${element.id})">修改</button></span>
                            </th>
                        `;
            tbody.appendChild(row);
        } else if (element.accuracy >= 60 && element.accuracy < 80 && element.total != null) {
            const row = document.createElement('tr');
            row.innerHTML = `
                            <th><input type="checkbox"></th>
                            <th><h4>${element.id}</h4></th>
                            <th><h4 style="color: rgb(96, 39, 129);font-size: 12.5px"><i>${element.word}</i></h4></th>
                            <th><h4 style="color: rgb(59, 59, 59);">${element.meaning}</h4></th>
                            <th><h4>${element.partOfSpeech}</h4></th>
                            <th><h4>${element.belongGrade}</h4></th>
                            <th><h4>${element.similarWord}</h4></th>
                            <th><h4>${element.phrase}</h4></th>
                            <th><div class="passPercent_middle"><h4>${element.accuracy}%</h4></div></th>
                            <th>
                                <span class="delete_span"><button onclick="deletePublicWord(${element.id})">删除</button></span>
                                <span class="revise_span"><button onclick="openUpdateBar(${element.id})">修改</button></span>
                            </th>
                        `;
            tbody.appendChild(row);
        } else if (element.accuracy < 60 && element.total != null) {
            const row = document.createElement('tr');
            row.innerHTML = `
                            <th><input type="checkbox"></th>
                            <th><h4>${element.id}</h4></th>
                            <th><h4 style="color: rgb(96, 39, 129);font-size: 12.5px"><i>${element.word}</i></h4></th>
                            <th><h4 style="color: rgb(59, 59, 59);">${element.meaning}</h4></th>
                            <th><h4>${element.partOfSpeech}</h4></th>
                            <th><h4>${element.belongGrade}</h4></th>
                            <th><h4>${element.similarWord}</h4></th>
                            <th><h4>${element.phrase}</h4></th>
                            <th><div class="passPercent_less"><h4>${element.accuracy}%</h4></div></th>
                            <th>
                                <span class="delete_span"><button onclick="deletePublicWord(${element.id})">删除</button></span>
                                <span class="revise_span"><button onclick="openUpdateBar(${element.id})">修改</button></span>
                            </th>
                        `;
            tbody.appendChild(row);
        } else if (element.total === null) {
            const row = document.createElement('tr');
            row.innerHTML = `
                            <th><input type="checkbox"></th>
                            <th><h4>${element.id}</h4></th>
                            <th><h4 style="color: rgb(96, 39, 129);font-size: 12.5px"><i>${element.word}</i></h4></th>
                            <th><h4 style="color: rgb(59, 59, 59);">${element.meaning}</h4></th>
                            <th><h4>${element.partOfSpeech}</h4></th>
                            <th><h4>${element.belongGrade}</h4></th>
                            <th><h4>${element.similarWord}</h4></th>
                            <th><h4>${element.phrase}</h4></th>
                            <th><div class="passPercent_null"><h4>暂无</h4></div></th>
                            <th>
                                <span class="delete_span"><button onclick="deletePublicWord(${element.id})">删除</button></span>
                                <span class="revise_span"><button onclick="openUpdateBar(${element.id})">修改</button></span>
                            </th>
                        `;
            tbody.appendChild(row);
        }
    });
}

//进入自动点击展示全部
//进入自动点击展示全部（延迟100ms，确保函数完全挂载）
setTimeout(() => {
    queryAllWordDoc.click();
}, 0);


//实时数据

//单词准确率数据展示
function accuracyExhibit() {
    var getAccuracyDoc = document.getElementById('accuracy');
    const token = localStorage.getItem('token')
    axios.get('http://localhost:8080/word/accuracy', {
        headers: {
            userToken: token
        }
    })
        .then(response => {
            const accuracy = response.data;
            getAccuracyDoc.innerHTML = accuracy;
        })
}

//新增单词总数
// http://localhost:8080/word/newWord
function getNewWordTotal() {
    var getTotalDoc = document.getElementById('newWord');
    const token = localStorage.getItem('token')
    axios.get('http://localhost:8080/word/newWord', {headers: {userToken: token}})
        .then(response => {
            const newWord = response.data;
            getTotalDoc.innerHTML = newWord;
        })
}

//官方单词总数
// http://localhost:8080/word/newWord
function getWordTotal() {
    var getTotalDoc = document.getElementById('total');
    axios.get('http://localhost:8080/word/wordTotal')
        .then(response => {
            const total = response.data;
            getTotalDoc.innerHTML = total;
        })
}


//获取易错单词
function getMistakeTotal() {
    var getmistakeDoc = document.getElementById('mistake');
    const token = localStorage.getItem('token');
    axios.get('http://localhost:8080/word/lessScore', {headers: {userToken: token}})
        .then(response => {
            const mistake = response.data;
            getmistakeDoc.innerHTML = mistake;
        })
}

//差距单词数据获取与渲染

function differentData() {
    const token = localStorage.getItem('token');
    axios.get('http://localhost:8080/word/differentData', {headers: {userToken: token}})
        .then(response => {
            const accuracyDifData = document.getElementById("accuracyDif");
            const newWordDifData = document.getElementById("newWordDif");
            const officialDifData = document.getElementById("officialDif");
            const lessDifData = document.getElementById("lessDif");
            const result = response.data;
            const allDifferentData = result.data;
            //渲染数据
            accuracyDifData.innerHTML = allDifferentData.accuracyDifferent + "  较昨天";
            newWordDifData.innerHTML = allDifferentData.newWordDifferent + "  较昨天";
            officialDifData.innerHTML = allDifferentData.officialsWordDifferent + "  较昨天";
            lessDifData.innerHTML = allDifferentData.lessDifferent + "  较昨天";
        })
}

setInterval(accuracyExhibit, 1000);
setInterval(getNewWordTotal, 1000);
setInterval(getWordTotal, 1000);
setInterval(getMistakeTotal, 1000);
setInterval(differentData, 1000);


// 以下是更新/修改单词处理
// 以下是更新/修改单词处理
// 以下是更新/修改单词处理
const updateCancelDoc = document.getElementById('updateCancel');
const updateConfirmDoc = document.getElementById('updateConfirm');
let wordId = 0;

// 显示更新栏
function openUpdateBar(id) {
    document.getElementById("update").style.display = "flex";
    axios.get('http://localhost:8080/word/publicById', {params: {id: id}}).then(response => {
        const wordUpdateDoc = document.getElementById('wordUpdate');
        const meaningUpdateDoc = document.getElementById('meaningUpdate');
        const partOfSpeechUpdateDoc = document.getElementById('partOfSpeechUpdate');
        const belongGradeUpdateDoc = document.getElementById('belongGradeUpdate');
        const similarWordUpdateDoc = document.getElementById('similarWordUpdate');
        const phraseUpdateDoc = document.getElementById('phraseUpdate');
        //得到结果
        const result = response.data;
        const word = result.data;
        wordUpdateDoc.value = word.word;
        meaningUpdateDoc.value = word.meaning;
        partOfSpeechUpdateDoc.value = word.partOfSpeech;
        belongGradeUpdateDoc.value = word.belongGrade;
        similarWordUpdateDoc.value = word.similarWord;
        phraseUpdateDoc.value = word.phrase;
        //给wordId赋值
        wordId = word.id;
    })
}

// 取消更新
function cancelUpdate() {
    document.getElementById("update").style.display = "none";
    return;
}

//确认更新/修改

function updateHandle() {
    //设置json格式
    const wordWithUserId = {
        userId: userId,
        id: wordId,
        word: document.getElementById('wordUpdate').value,
        meaning: document.getElementById('meaningUpdate').value,
        partOfSpeech: document.getElementById('partOfSpeechUpdate').value,
        belongGrade: document.getElementById('belongGradeUpdate').value,
        similarWord: document.getElementById('similarWordUpdate').value,
        phrase: document.getElementById('phraseUpdate').value
    };
    axios.post('http://localhost:8080/word/update', wordWithUserId).then(response => {
        const result = response.data;
        if (result === 1) {
            alert("id为 " + wordId + " 的单词修改成功！！")
            wordId = 0;
            cancelUpdate();
            queryAllWordDoc.click();
            return;
        } else {
            alert("id为 " + wordId + " 的单词修改失败！！")
            wordId = 0;
            cancelUpdate();
            queryAllWordDoc.click();
            return;
        }
    });

}


//新增页面显示
//新增页面显示
//新增页面显示
function openAddBar() {
    document.getElementById('add').style.display = "flex";
}

//处理添加
function addHandle() {
    //设置json格式
    const addPublicBody = {
        userId: userId,
        id: wordId,
        word: document.getElementById('wordAdd').value,
        meaning: document.getElementById('meaningAdd').value,
        partOfSpeech: document.getElementById('partOfSpeechAdd').value,
        belongGrade: document.getElementById('belongGradeAdd').value,
        similarWord: document.getElementById('similarWordAdd').value,
        phrase: document.getElementById('phraseAdd').value
    };
    axios.post('http://localhost:8080/word/publicAdd', addPublicBody).then(response => {
        const result = response.data;
        if (result === 1) {
            alert("id为 " + wordId + " 的单词添加成功！！")
            wordId = 0;
            cancelUpdate();
            queryAllWordDoc.click();
            cancelAdd();
            return;
        } else {
            alert("id为 " + wordId + " 的单词添加失败！！")
            wordId = 0;
            cancelUpdate();
            queryAllWordDoc.click();
            return;
        }
    });

}

//取消显示
function cancelAdd() {
    document.getElementById('add').style.display = "none";
}


//删除公共单词 根据单词id
//删除公共单词 根据单词id
//删除公共单词 根据单词id
function deletePublicWord(id) {
    alert("当前要删除的单词是" + id);
    axios.delete('http://localhost:8080/word/deletePublicWord', {params: {id: id}})
        .then(response => {
            const result = response.data;
            const msg = result.data;
            //提示删除
            alert(msg);
            queryAllWordDoc.click();
        });
}

window.deletePublicWord = deletePublicWord;

//单词查询处理
const wordDoc = document.getElementById('word');
var meaningDoc = document.getElementById('meaning');
var wordClassDoc = document.getElementById('wordClass');
var selectDegradDoc = document.getElementById('selectDegrad');
var queryAttachConditionDoc = document.getElementById('queryAttachCondition');

//清空内容
function emptyInput() {
    wordDoc.value = "";
    meaningDoc.value = "";
    wordClassDoc.value = "";
    selectDegradDoc.value = "";
}

queryAttachConditionDoc.addEventListener('click', async () => {
    const page = document.getElementById('page');
    const pageDoc = page.value;
    const pageSize = document.getElementById('pageSizeSelect');
    const pageSizeDoc = pageSize.value;
    const wordV = wordDoc.value === "" ? null : wordDoc.value.trim();
    const meaningV = meaningDoc.value === "" ? null : meaningDoc.value.trim();
    const wordClassV = wordClassDoc.value === "" ? null : wordClassDoc.value.trim();
    const selectDegradV = null;
    const queryBody = {
        userId: userId,
        page: pageDoc,
        size: pageSizeDoc,
        word: wordV,
        meaning: meaningV,
        wordClass: wordClassV,
        selectDegrad: selectDegradV
    }

    const response = await axios.post('http://localhost:8080/word/queryWord', queryBody)
    const data = response.data;
    const words = data.data;
    const getHasSearchTotal = data.hasSearchTotal;
    var getHasSearchTotalDoc = document.getElementById('hasSearchTotal');
    getHasSearchTotalDoc.innerHTML = getHasSearchTotal;
    wordBaseShowFunc(words);
})


/*以为之前的单词渲染函数包含 "准确率" 等这些数据，
* 导致可能后端缓存查出来可能渲染不了，
* 所以统一写一个单词基础信息渲染函数*/

function wordBaseShowFunc(words) {
    //获取所有数据
    const tbody = document.querySelector('#wordTable tbody');
    tbody.innerHTML = '';

    //将数据逐行用tr为单位渲染出来
    words.forEach(element => {
        //根据准确率判断不同样式的tr
        const row = document.createElement('tr');
        row.innerHTML = `
                    <th><input type="checkbox"></th>
                    <th><h4>${element.id}</h4></th>
                    <th><h4 style="color: rgb(96, 39, 129);font-size: 12.5px"><i>${element.word}</i></h4></th>
                    <th><h4 style="color: rgb(59, 59, 59);">${element.meaning}</h4></th>
                    <th><h4>${element.partOfSpeech}</h4></th>
                    <th><h4>${element.belongGrade}</h4></th>
                    <th><h4>${element.similarWord}</h4></th>
                    <th><h4>${element.phrase}</h4></th>
                        `;
        tbody.appendChild(row);
    });
}


/*
* 上一页和下一页功能*/
var previousPageDoc = document.getElementById('previousPage'); //上一页按钮
var nextPagePageDoc = document.getElementById('nextPage'); //下一页按钮
var pageDoc = document.getElementById('page'); //页数input
const pageSize = document.getElementById('pageSizeSelect'); //每页数量


//下一页点击处理
nextPagePageDoc.addEventListener('click', async () => {
    var pageValue = parseInt(pageDoc.value, 10);
    if (pageValue === saveTotalPage) { // 达到极限
        const pageEndValue = pageValue; //上传页码结果
        pageDoc.value = pageEndValue;
        const pageSizeValue = pageSize.value; //每页数量结果

        //保存当前页码到全局变量
        currentPage = pageEndValue;

        //http响应
        const token = localStorage.getItem('token') //得到token
        const response = await axios.get('http://localhost:8080/word/page', {
            headers: {userToken: token},
            params: {page: pageEndValue, pageSize: pageSizeValue}
        })
        const getResult = response.data; //获得所有数据
        const words = getResult.data; //获得所有单词
        const hasSearchTotal = getResult.hasSearchTotal; //获得已经查询的数量
        //先将查询的总数渲染在页面
        var getHasSearchTotalDoc = document.getElementById('hasSearchTotal');
        getHasSearchTotalDoc.innerHTML = hasSearchTotal;

        //当前页数
        var currentPageDoc = document.getElementById('currentPage');
        currentPageDoc.innerHTML = pageEndValue;

        //当前页数全局值
        currentPage = pageEndValue;

        //这里将数据转化成table中的tbody，使用函数
        createWordTbody(words);
        //判断保存按钮是否要保存页码
        if (switchDom.checked === true) {
            try {
                // 1. 获取并校验Token
                const token = localStorage.getItem("token");

                // 2. 直接用前端当前的开关状态（无需请求后端，避免NaN）
                const currentChecked = switchDom.checked;
                const switchDomResult = Number(currentChecked); // true→1，false→0
                const savePageResult = parseInt(currentPage) || 1; // 兜底：确保页码有效

                // 3. 构造请求体
                const settingBody = {
                    isSavePage: switchDomResult,
                    savePage: savePageResult,
                };

                // 4. 发送更新请求（异步/await 更易读）
                const response = await axios.put('http://localhost:8080/setting', settingBody, {
                    headers: {userToken: token}
                });
                const result = response.data;
                const data = result.data || {};

                // 5. 确认状态（优先用前端操作的状态，避免后端返回异常值）
                switchDom.checked = currentChecked;

            } catch (error) {
            }
        }
    } else {
        const pageEndValue = pageValue + 1;

        // ========== 关键修复1：pageSize转数字+兜底 ==========
        const pageSizeValue = Number(pageSize.value) || 10; // 强制转数字，兜底默认10
        let response;

        // ========== 关键修复2：加外层try/catch，捕获所有错误 ==========
        try {
            // 1. 校验token
            const token = localStorage.getItem('token');
            if (!token) {
                throw new Error('token不存在！请重新登录');
            }

            // 2. 发送请求（参数传数字）
            response = await axios.get('http://localhost:8080/word/nextPage', {
                headers: {userToken: token},
                params: {
                    page: pageEndValue, // 数字
                    pageSize: pageSizeValue // 数字，关键！
                }
            });

            // ========== 关键修复3：校验返回数据有效性 ==========
            if (!response.data) {
                throw new Error('后端返回空数据');
            }
            const getResult = response.data;
            // 兜底：避免data/hasSearchTotal不存在导致报错
            const words = getResult.data || [];

            //渲染数据
            createWordTbody(words);
            const hasSearchTotal = getResult.hasSearchTotal || 0;

            // ========== 关键修复4：校验DOM元素是否存在 ==========
            const getHasSearchTotalDoc = document.getElementById('hasSearchTotal');
            const currentPageDoc = document.getElementById('currentPage');
            if (!getHasSearchTotalDoc || !currentPageDoc) {
                throw new Error('页面DOM元素缺失（hasSearchTotal/currentPage未找到）');
            }

            // 3. 执行完请求，再更新页面（你的逻辑）
            pageDoc.value = pageEndValue;
            getHasSearchTotalDoc.innerHTML = hasSearchTotal;
            currentPageDoc.innerHTML = pageEndValue;
            currentPage = pageEndValue; // 只赋值一次，移除重复赋值

            // queryAllWordDoc.click(); //不渲染直接绕过

            // 5. 保存页码（补全错误日志，不再空catch）
            if (switchDom.checked === true) {
                try {
                    const token = localStorage.getItem("token");
                    const currentChecked = switchDom.checked;
                    const switchDomResult = Number(currentChecked);
                    const savePageResult = parseInt(currentPage) || 1;

                    const settingBody = {
                        isSavePage: switchDomResult,
                        savePage: savePageResult
                    };

                    await axios.put('http://localhost:8080/setting', settingBody, {
                        headers: {userToken: token}
                    });
                    switchDom.checked = currentChecked;
                } catch (error) {
                    // 关键：不再空catch，打印保存页码的错误
                    console.error('保存页码失败：', error);
                }
            }

        } catch (error) {
            // ========== 关键修复5：捕获所有错误，给出明确提示 ==========
            console.error('下一页逻辑执行失败：', error); // 控制台打印详细错误
            alert(`操作失败！原因：${error.message}\n（请打开F12控制台看更多详情）`);
            // 失败后回滚全局状态，避免混乱
            currentPage = pageValue;
            pageDoc.value = pageValue;
        }
    }
});

//上一页点击处理
previousPageDoc.addEventListener('click', async () => {
    var pageValue = parseInt(pageDoc.value, 10);
    if (pageValue === 1) {
        pageDoc.value = 1;

        // 核心新增：主动请求第一页数据
        const token = localStorage.getItem('token'); // 获取token
        const pageSizeValue = pageSize.value || 10; // 每页数量兜底
        if (token) { // 有token才请求，避免报错
            // 发送请求获取第一页数据（和其他页码的请求逻辑一致）
            axios.get('http://localhost:8080/word/page', {
                headers: {userToken: token},
                params: {page: 1, pageSize: pageSizeValue}
            }).then(response => {
                const getResult = response.data;
                const words = getResult.data; // 第一页的真实数据
                const hasSearchTotal = getResult.hasSearchTotal;

                // 更新查询总数显示
                var getHasSearchTotalDoc = document.getElementById('hasSearchTotal');
                getHasSearchTotalDoc.innerHTML = hasSearchTotal;

                // 更新当前页码显示
                var currentPageDoc = document.getElementById('currentPage');
                currentPageDoc.innerHTML = 1;

                // 更新表格数据（关键：用第一页数据替换旧数据）
                createWordTbody(words);

                // 可选：如果开关开启，保存第一页页码到后端
                if (switchDom?.checked === true) {
                    try {
                        const settingBody = {
                            isSavePage: Number(switchDom.checked),
                            savePage: 1
                        };
                        axios.put('http://localhost:8080/setting', settingBody, {
                            headers: {userToken: token}
                        });
                    } catch (error) {
                        console.error('保存第一页页码失败：', error);
                    }
                }
            }).catch(error => {
                console.error('请求第一页数据失败：', error);
            });
        }

        return; // 最后再return，确保请求逻辑执行完
    }
    const pageEndValue = pageValue - 1; ////上传页码结果
    pageDoc.value = pageEndValue;
    const pageSizeValue = pageSize.value;

    //http响应
    const token = localStorage.getItem('token') //得到token
    const response = await axios.get('http://localhost:8080/word/onPage', {
        headers: {userToken: token},
        params: {page: pageEndValue, pageSize: pageSizeValue}
    });
    const getResult = response.data; //获得所有数据
    const words = getResult.data; //获得所有单词
    const hasSearchTotal = getResult.hasSearchTotal; //获得已经查询的数量
    //先将查询的总数渲染在页面
    var getHasSearchTotalDoc = document.getElementById('hasSearchTotal');
    getHasSearchTotalDoc.innerHTML = hasSearchTotal;

    //当前页数
    var currentPageDoc = document.getElementById('currentPage');
    currentPageDoc.innerHTML = pageEndValue;

    //当前页数全局值
    currentPage = pageEndValue;

    //这里将数据转化成table中的tbody，使用函数
    createWordTbody(words);

    //保存当前页码到全局变量
    currentPage = pageEndValue;

    // queryAllWordDoc.click();

    if (switchDom.checked === true) {
        try {
            // 1. 获取并校验Token
            const token = localStorage.getItem("token");

            // 2. 直接用前端当前的开关状态（无需请求后端，避免NaN）
            const currentChecked = switchDom.checked;
            const switchDomResult = Number(currentChecked); // true→1，false→0
            const savePageResult = parseInt(currentPage) || 1; // 兜底：确保页码有效

            // 3. 构造请求体
            const settingBody = {
                isSavePage: switchDomResult,
                savePage: savePageResult
            };

            // 4. 发送更新请求（异步/await 更易读）
            const response = await axios.put('http://localhost:8080/setting', settingBody, {
                headers: {userToken: token}
            });
            const result = response.data;
            const data = result.data || {};

            // 5. 确认状态（优先用前端操作的状态，避免后端返回异常值）
            switchDom.checked = currentChecked;

        } catch (error) {
        }
    }
});


















