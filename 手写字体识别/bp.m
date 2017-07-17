% �ű� ʹ��newff����ʵ���Ա�ʶ��
% main_newff.m

%% ����
clear,clc
rng('default')
rng(2)

%% ��������
load('mnist_train_images.mat');
load('mnist_train_labels.mat');
load('mnist_test_images.mat');
load('mnist_test_labels.mat');


%trainImages = loadMNISTImages('train-images.idx3-ubyte');      
%trainLabels = loadMNISTLabels('train-labels.idx1-ubyte');

%testImages = loadMNISTImages('t10k-images.idx3-ubyte');
%testLabels = loadMNISTLabels('t10k-labels.idx1-ubyte');

%% ��������
%[traind,trainl,testd,testl]=divide(data,label);

%% ��������
net=feedforwardnet(30);
net.trainFcn='trainscg';
net.trainParam.goal=0.01                    % ѵ��Ŀ����С����������Ϊ0.1
net.trainParam.epochs=500;             % ѵ����������������Ϊ300��
net.trainParam.show=20;                   % ��ʵƵ�ʣ���������Ϊûѵ��20����ʾһ��
net.trainParam.mc=0.95;                    % ���Ӷ�������
net.trainParam.lr=0.1;                       % ѧϰ���ʣ���������Ϊ0.05
net.trainParam.min_grad=1e-6;        % ��С�����ݶ�
net.trainParam.min_fail=5;                 % ���ȷ��ʧ�ܴ���

%% ѵ������
net=train(net,mnist_train_images',mnist_train_labels');

%% ����
test_out=sim(net,mnist_test_images');
test_out =test_out' ;
test_out(test_out>=0.5)=1;
test_out(test_out<0.5)=0;
rate=sum(test_out==mnist_test_labels)/length(mnist_test_labels);
fprintf('  ��ȷ��\n   %f %%\n', rate*100);