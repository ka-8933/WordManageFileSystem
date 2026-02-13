
//用户id


// 用户数据Echart
var userDataEchart = echarts.init(document.getElementById('userData'));

function loadUserDataEchart(){
    axios.get('http://localhost:8080/report/dataOverLook')
        .then(response => {
            const result = response.data;
            const userDataEchartData = result.data;

            userDataEchartOption = {
                title: {},
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: ['四天前', '三天前', '两天前', '一天前', '今天']
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                toolbox: {
                    feature: {
                        saveAsImage: {}
                    }
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: ['四天前', '三天前', '两天前', '一天前', '今天']
                },
                yAxis: {
                    type: 'value'
                },
                series: userDataEchartData

            };

            userDataEchart.setOption(userDataEchartOption);
        })
}

//开局自动启动函数
document.addEventListener('DOMContentLoaded' , function (){
    loadUserDataEchart();
})

//数据总览文本数据分配
const wordTotalDoc = document.getElementById('wordTotal');
const accuracyAvgDoc = document.getElementById('accuracyAvg');
const reedAvgDoc = document.getElementById('reedAvg');
const userTotalDoc = document.getElementById('userTotal');

function overLookText(){
    axios.get('http://localhost:8080/report/overLookText')
        .then(response => {
            const result = response.data;
            const data = result.data;
            const wordTotal = data.wordTotal;
            const userAccuracyAvg = data.userAccuracyAvg;
            const userReedAvg = data.userReedAvg;
            const userTotal = data.userTotal;

            wordTotalDoc.innerHTML = wordTotal;
            accuracyAvgDoc.innerHTML = userAccuracyAvg;
            reedAvgDoc.innerHTML = userReedAvg;
            userTotalDoc.innerHTML = userTotal;
        })
}

document.addEventListener('DOMContentLoaded' , function (){
    overLookText();
})

// 单词抽查数据
// 单词抽查数据
// 单词抽查数据

var checkDataEchart = echarts.init(document.getElementById('checkData'));

function checkDailyDataEchart(){
    const token = localStorage.getItem('token');
    if (token === null){
        alert("token 为null")
    }
    axios.get('http://localhost:8080/report/checkEchart' , {headers : {userToken : token}})
        .then(response => {
            const result = response.data;
            const dataEchart = result.data;

            checkDataEchartOption = {
                title: {
                    text: '每日单词抽查'
                },
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'shadow'
                    }
                },
                legend: {},
                xAxis: {
                    type: 'value',
                    boundaryGap: [0, 0.01]
                },
                yAxis: {
                    type: 'category',
                    data: ['准确数', '错误数', '总次数']
                },
                series: dataEchart
            };

            checkDataEchart.setOption(checkDataEchartOption);
        })
}


document.addEventListener('DOMContentLoaded' , function (){
    checkDailyDataEchart();
})

// 用户准确率数据
var accurcyDataEchart = echarts.init(document.getElementById('accurcyData'));

function loadAccuracyEChartData(){
    const token = localStorage.getItem('token');
    if (token === null){
        alert("token 为null")
    }
    axios.get('http://localhost:8080/report/accuracyEchart' , {headers : {userToken : token}})
        .then(response => {
            const result = response.data;
            const accurcyDataEchartData = result.data;

            accurcyDataEchartOption = {
                tooltip: {
                    trigger: 'item'
                },
                legend: {
                    top: '5%',
                    left: 'center'
                },
                series: [
                    {
                        name: 'Access From',
                        type: 'pie',
                        radius: ['40%', '70%'],
                        center: ['50%', '70%'],
                        // adjust the start and end angle
                        startAngle: 180,
                        endAngle: 360,
                        data: accurcyDataEchartData
                    }
                ]
            };

            accurcyDataEchart.setOption(accurcyDataEchartOption);
        })
}

document.addEventListener('DOMContentLoaded' , function (){
    loadAccuracyEChartData();
})


//单词本单词数量
//单词本单词数量
//单词本单词数量
var noteNumberEchart = echarts.init(document.getElementById('noteNumber'));

noteNumberEchartOption = {
    grid: {
        left: '10%',    // 绘图区距离容器左侧的距离
        right: '10%',   // 绘图区距离容器右侧的距离
        top: '10%',     // 绘图区距离容器顶部的距离
        bottom: '10%',  // 绘图区距离容器底部的距离
        containLabel: true // 重要：确保坐标轴的标签在grid区域内
    },
    xAxis: {
        type: 'category',
        data: ['单词本1', '单词本2', '单词本3', '单词本4']
    },
    yAxis: {
        type: 'value'
    },
    series: [
        {
            data: [342, 245, 631],
            type: 'bar'
        }
    ]
};

noteNumberEchart.setOption(noteNumberEchartOption);


// 阅读题准确率
var reedAccuracyEchart = echarts.init(document.getElementById('reedAccuracy'));

reedAccuracyEchartOption = {
    tooltip: {
        trigger: 'item'
    },
    // 删除整个 legend 配置
    series: [
        {
            name: 'Access From',
            type: 'pie',
            radius: '60%',
            data: [
                {value: 95, name: '初中'},
                {value: 84, name: '高中'},
                {value: 80, name: '四级'},
                {value: 65, name: '六级'},
                {value: 45, name: '雅思'}
            ],
            emphasis: {
                itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
        }
    ]
};

reedAccuracyEchart.setOption(reedAccuracyEchartOption);


// 阅读数量
var reedNumberEchart = echarts.init(document.getElementById('reedNumber'));
reedNumberEchartOption = {
    grid: {
        left: '10%',    // 绘图区距离容器左侧的距离
        right: '10%',   // 绘图区距离容器右侧的距离
        top: '10%',     // 绘图区距离容器顶部的距离
        bottom: '10%',  // 绘图区距离容器底部的距离
        containLabel: true // 重要：确保坐标轴的标签在grid区域内
    },
    xAxis: {
        type: 'category',
        data: ['四天前', '三天前', '两天前', '一天前', '今天']
    },
    yAxis: {
        type: 'value'
    },
    series: [
        {
            data: [2, 3, 3, 4, 2],
            type: 'line'
        }
    ]
};

reedNumberEchart.setOption(reedNumberEchartOption);


// 用户总和得分
var scoreEchart = echarts.init(document.getElementById('score'));

scoreEchartOption = {

    radar: {
        // shape: 'circle',
        indicator: [
            {name: '阅读能力', max: 100},
            {name: '单词抽查', max: 100},
            {name: '持之以恒', max: 100},
            {name: '持之以恒', max: 100},
            {name: '持之以恒', max: 100},
        ]
    },
    series: [
        {
            name: 'Budget vs spending',
            type: 'radar',
            data: [
                {
                    value: [34, 78, 98, 98, 65],
                    name: '综合评分'
                },
                {
                    value: [61.56, 34, 75, 74],
                    name: '平均'
                }
            ]
        }
    ]
};

scoreEchart.setOption(scoreEchartOption);

// 第三大栏三等分数据

//数据1
var chuGaoEchart = echarts.init(document.getElementById('chuGao'));

chuGaoEchartOption = {
    grid: {
        top: '0%',    // 上边距，可以调整这个值让图形上移
        left: '10%',
        right: '10%',
        bottom: '30%'
    },
    legend: {
        top: '93%',
        button: '5%',
        left: 'right'
    },
    series: [
        {
            name: 'Access From',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2
            },
            label: {
                show: true,
            },
            emphasis: {
                label: {
                    show: true,
                    fontSize: 12,
                    fontWeight: 'bold'
                }
            },
            labelLine: {
                show: false
            },
            data: [
                {value: 1048, name: '初高中'},
                {value: 735, name: '四六级'},
                {value: 580, name: '雅思'},
            ]
        }
    ]
};

chuGaoEchart.setOption(chuGaoEchartOption);

//数据2
var siLiuEchart = echarts.init(document.getElementById('siLiu'));

siLiuEchartOption = {
    grid: {
        top: '0%',    // 上边距，可以调整这个值让图形上移
        left: '10%',
        right: '10%',
        bottom: '30%'
    },
    legend: {
        top: '93%',
        button: '5%',
        left: 'right'
    },
    series: [
        {
            name: 'Access From',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2
            },
            label: {
                show: true,
            },
            emphasis: {
                label: {
                    show: true,
                    fontSize: 12,
                    fontWeight: 'bold'
                }
            },
            labelLine: {
                show: false
            },
            data: [
                {value: 1048, name: '初高中'},
                {value: 735, name: '四六级'},
                {value: 580, name: '雅思'},
            ]
        }
    ]
};

siLiuEchart.setOption(siLiuEchartOption);

//数据3
var yasiEchart = echarts.init(document.getElementById('yasi'));

yasiEchartOption = {
    grid: {
        top: '0%',    // 上边距，可以调整这个值让图形上移
        left: '10%',
        right: '10%',
        bottom: '30%'
    },
    legend: {
        top: '93%',
        button: '5%',
        left: 'right'
    },
    series: [
        {
            name: 'Access From',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2
            },
            label: {
                show: true,
            },
            emphasis: {
                label: {
                    show: true,
                    fontSize: 12,
                    fontWeight: 'bold'
                }
            },
            labelLine: {
                show: false
            },
            data: [
                {value: 1048, name: '初高中'},
                {value: 735, name: '四六级'},
                {value: 580, name: '雅思'},
            ]
        }
    ]
};

yasiEchart.setOption(yasiEchartOption);