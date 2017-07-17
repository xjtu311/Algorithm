clear;clc;
%%
mnist_train_images = loadMNISTImages('train-images.idx3-ubyte');      
mnist_train_labels = loadMNISTLabels('train-labels.idx1-ubyte');
mnist_test_images = loadMNISTImages('t10k-images.idx3-ubyte');
mnist_test_labels = loadMNISTLabels('t10k-labels.idx1-ubyte');
%% SVM����ѵ��
[mnist_train_images,MU,SIGMA] = zscore(mnist_train_images');
model = svmtrain(mnist_train_labels,mnist_train_images); 
%% SVM����Ԥ��
[mnist_test_images,MU,SIGMA] = zscore(mnist_test_images');
[predict_label,accuracy,decision_value] = svmpredict(mnist_test_labels, mnist_test_images, model);
%% �������
% ���Լ���ʵ�ʷ����Ԥ�����ͼ
% ͨ��ͼ���Կ���ֻ��һ�����������Ǳ���ֵ�
figure;
hold on;
plot(mnist_test_labels,'o');
plot(predict_label,'r*');
xlabel('���Լ�����','FontSize',12);
ylabel('����ǩ','FontSize',12);
legend('ʵ�ʲ��Լ�����','Ԥ����Լ�����');
title('���Լ���ʵ�ʷ����Ԥ�����ͼ','FontSize',12);
grid on;