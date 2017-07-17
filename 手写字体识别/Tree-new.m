% trainImageSvm为样本输入数据集  trainLableSvm为样本目标数据集
	% testImageSvm为测试输入数据集  testLableSvm为测试目标数据集
clear;clc;clf; 
mnist_train_images = loadMNISTImages('train-images.idx3-ubyte');      
mnist_train_labels = loadMNISTLabels('train-labels.idx1-ubyte');
mnist_test_images = loadMNISTImages('t10k-images.idx3-ubyte');
mnist_test_labels = loadMNISTLabels('t10k-labels.idx1-ubyte');



data = mnist_train_images(:,1:100);
label = mnist_train_labels(1:100,:);
testdata = mnist_test_images(:,1:10000);
testlabel = mnist_test_labels(1:10000,:);

[data,MU,SIGMA] = zscore(data');
t = classregtree(data,label);
view(t);


[testdata,MU,SIGMA] = zscore(testdata');
    [COST,SECOST,NTNODES,BESTLEVEL]= test(t,'test',testdata,testlabel);
    
    [c,s,n,best] = test(t,'cross',testdata,testlabel);
tmin = prune(t,'level',best);
% Plot smallest tree within 1 std. error of minimum cost tree
[mincost,minloc] = min(c);
plot(n,c,'b-o', n(best+1),c(best+1),'bs',...
            n,(mincost+s(minloc))*ones(size(n)),'k--');
xlabel('Tree size (number of terminal nodes)');
ylabel('Cost');

