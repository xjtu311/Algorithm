%% ����
clear,clc
rng('default')
rng(2)
%% ��������

load('mnist_train_images.mat');
load('mnist_train_labels.mat');
load('mnist_test_images.mat');
load('mnist_test_labels.mat');

trainImages = mnist_train_images(1:1000,:);
trainLabels = mnist_train_labels(1:1000,:);
testImages = mnist_test_images(1:1000,:);
testLabels = mnist_test_labels(1:1000,:);


%% ��������
net=feedforwardnet(30);
net.trainFcn='trainscg';
net.trainParam.goal=0.01;                    % ѵ��Ŀ����С����������Ϊ0.1
net.trainParam.epochs=500;             % ѵ����������������Ϊ300��
net.trainParam.lr=0.1;                       % ѧϰ���ʣ���������Ϊ0.1
net.trainParam.min_grad=1e-6;        % ��С�����ݶ�


%% ѵ������
net=train(net,trainImages',trainLabels');

%% ����
test_out=sim(net,testImages');
test_out = test_out' ;
test_out(test_out>=0.5)=1;
test_out(test_out<0.5)=0;
rate=sum(test_out==testLabels)/length(testLabels);
fprintf('  ��ȷ��\n   %f %%\n', rate*100);

