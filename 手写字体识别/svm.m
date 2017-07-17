clear;clc;
%%
mnist_train_images = loadMNISTImages('train-images.idx3-ubyte');      
mnist_train_labels = loadMNISTLabels('train-labels.idx1-ubyte');
mnist_test_images = loadMNISTImages('t10k-images.idx3-ubyte');
mnist_test_labels = loadMNISTLabels('t10k-labels.idx1-ubyte');
%% SVM网络训练
[mnist_train_images,MU,SIGMA] = zscore(mnist_train_images');
model = svmtrain(mnist_train_labels,mnist_train_images); 
%% SVM网络预测
[mnist_test_images,MU,SIGMA] = zscore(mnist_test_images');
[predict_label,accuracy,decision_value] = svmpredict(mnist_test_labels, mnist_test_images, model);
%% 结果分析
% 测试集的实际分类和预测分类图
% 通过图可以看出只有一个测试样本是被错分的
figure;
hold on;
plot(mnist_test_labels,'o');
plot(predict_label,'r*');
xlabel('测试集样本','FontSize',12);
ylabel('类别标签','FontSize',12);
legend('实际测试集分类','预测测试集分类');
title('测试集的实际分类和预测分类图','FontSize',12);
grid on;