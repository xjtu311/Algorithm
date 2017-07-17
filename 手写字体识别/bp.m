% 脚本 使用newff函数实现性别识别
% main_newff.m

%% 清理
clear,clc
rng('default')
rng(2)

%% 读入数据
load('mnist_train_images.mat');
load('mnist_train_labels.mat');
load('mnist_test_images.mat');
load('mnist_test_labels.mat');


%trainImages = loadMNISTImages('train-images.idx3-ubyte');      
%trainLabels = loadMNISTLabels('train-labels.idx1-ubyte');

%testImages = loadMNISTImages('t10k-images.idx3-ubyte');
%testLabels = loadMNISTLabels('t10k-labels.idx1-ubyte');

%% 划分数据
%[traind,trainl,testd,testl]=divide(data,label);

%% 创建网络
net=feedforwardnet(30);
net.trainFcn='trainscg';
net.trainParam.goal=0.01                    % 训练目标最小误差，这里设置为0.1
net.trainParam.epochs=500;             % 训练次数，这里设置为300次
net.trainParam.show=20;                   % 现实频率，这里设置为没训练20次显示一次
net.trainParam.mc=0.95;                    % 附加动量因子
net.trainParam.lr=0.1;                       % 学习速率，这里设置为0.05
net.trainParam.min_grad=1e-6;        % 最小性能梯度
net.trainParam.min_fail=5;                 % 最大确认失败次数

%% 训练网络
net=train(net,mnist_train_images',mnist_train_labels');

%% 测试
test_out=sim(net,mnist_test_images');
test_out =test_out' ;
test_out(test_out>=0.5)=1;
test_out(test_out<0.5)=0;
rate=sum(test_out==mnist_test_labels)/length(mnist_test_labels);
fprintf('  正确率\n   %f %%\n', rate*100);