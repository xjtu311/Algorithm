clear;
clc;
clear;
clc;
%%
mnist_train_images = loadMNISTImages('train-images.idx3-ubyte');      
mnist_train_labels = loadMNISTLabels('train-labels.idx1-ubyte');
mnist_test_images = loadMNISTImages('t10k-images.idx3-ubyte');
mnist_test_labels = loadMNISTLabels('t10k-labels.idx1-ubyte');

%% ѵ��
data = mnist_train_images(:,1:2000);
label = mnist_train_labels(1:2000,:);
    [data,MU,SIGMA] = zscore(data');
    model = svmtrain(label,data);
%% ����
testdata = mnist_test_images(:,1:2000);
testlabel = mnist_test_labels(1:2000,:);
    [testdata,MU,SIGMA] = zscore(testdata');
    [predictlabel,accuracy,decision_value] = svmpredict(testlabel,testdata,model);
 
%% �������
 
% ���Լ���ʵ�ʷ����Ԥ�����ͼ
% ͨ��ͼ���Կ���ֻ��һ�����������Ǳ���ֵ�
figure;
hold on;
plot(testlabel,'o');
plot(predictlabel,'r*');
xlabel('���Լ�����','FontSize',12);
ylabel('����ǩ','FontSize',12);
legend('ʵ�ʲ��Լ�����','Ԥ����Լ�����');
title('���Լ���ʵ�ʷ����Ԥ�����ͼ','FontSize',12);
grid on;