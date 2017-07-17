clear;
clc;
clear;
clc;
%%
mnist_train_images = loadMNISTImages('train-images.idx3-ubyte');      
mnist_train_labels = loadMNISTLabels('train-labels.idx1-ubyte');
mnist_test_images = loadMNISTImages('t10k-images.idx3-ubyte');
mnist_test_labels = loadMNISTLabels('t10k-labels.idx1-ubyte');

%% 训练
data = mnist_train_images(:,1:2000);
label = mnist_train_labels(1:2000,:);
    [data,MU,SIGMA] = zscore(data');
    model = svmtrain(label,data);
%% 测试
testdata = mnist_test_images(:,1:2000);
testlabel = mnist_test_labels(1:2000,:);
    [testdata,MU,SIGMA] = zscore(testdata');
    [predictlabel,accuracy,decision_value] = svmpredict(testlabel,testdata,model);
 
%% 结果分析
 
% 测试集的实际分类和预测分类图
% 通过图可以看出只有一个测试样本是被错分的
figure;
hold on;
plot(testlabel,'o');
plot(predictlabel,'r*');
xlabel('测试集样本','FontSize',12);
ylabel('类别标签','FontSize',12);
legend('实际测试集分类','预测测试集分类');
title('测试集的实际分类和预测分类图','FontSize',12);
grid on;