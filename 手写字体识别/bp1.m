%% 清理
clear,clc
rng('default')
rng(2)
%% 读入数据

load('mnist_train_images.mat');
load('mnist_train_labels.mat');
load('mnist_test_images.mat');
load('mnist_test_labels.mat');

trainImages = mnist_train_images(1:1000,:);
trainLabels = mnist_train_labels(1:1000,:);
testImages = mnist_test_images(1:1000,:);
testLabels = mnist_test_labels(1:1000,:);


%% 创建网络
net=feedforwardnet(30);
net.trainFcn='trainscg';
net.trainParam.goal=0.01;                    % 训练目标最小误差，这里设置为0.1
net.trainParam.epochs=500;             % 训练次数，这里设置为300次
net.trainParam.lr=0.1;                       % 学习速率，这里设置为0.1
net.trainParam.min_grad=1e-6;        % 最小性能梯度


%% 训练网络
net=train(net,trainImages',trainLabels');

%% 测试
test_out=sim(net,testImages');
test_out = test_out' ;
test_out(test_out>=0.5)=1;
test_out(test_out<0.5)=0;
rate=sum(test_out==testLabels)/length(testLabels);
fprintf('  正确率\n   %f %%\n', rate*100);

