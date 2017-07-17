%
% 用BP学习算法对鸢尾属植物样本集进行模式分类
% 数据库：Iris 
%
function Iris_classify()

InDim=2;
OutDim=3;

%获取训练样本：BPN输入（特征）和目标输出（类别）
M=csvread('Iris.csv');
M11=M(21:50,2);M12=M(71:100,2);M13=M(121:150,2);
M21=M(21:50,4);M22=M(71:100,4);M23=M(121:150,4);
%M11=M(1:20,2);M12=M(51:70,2);M13=M(101:120,2);
%M21=M(1:20,4);M22=M(51:70,4);M23=M(101:120,4);
M1=[M11' M12' M13']';
M2=[M21' M22' M23']';
SI=[M1 M2]';
[xx,SN]=size(SI);

for i=1:30
    MO1(:,i)=[1 0 0]';
    MO2(:,i)=[0 1 0]';
    MO3(:,i)=[0 0 1]';
end
SO=[MO1 MO2 MO3];

HUN=20;%隐含层节点个数
Maxsteps=20000;%最大学习次数
lrate=0.1;%学习速率
E0=0.01;%目标误差

%设定初始权值
W1=0.2*rand(HUN,InDim)-0.1;
B1=0.2*rand(HUN,1)-0.1;
W2=0.2*rand(OutDim,HUN)-0.1;
B2=0.2*rand(OutDim,1)-0.1;

W1Ex=[W1 B1];
W2Ex=[W2 B2];

SIE=[SI' ones(SN,1)]';
ErrRecord=[];
for i=1:Maxsteps
    
    HO=logsig(W1Ex*SIE);
    HOE=[HO' ones(SN,1)]';
    NetworkO=logsig(W2Ex*HOE);
    
    Error=SO-NetworkO;
    SSE=sumsqr(Error)
    
    ErrRecord=[ErrRecord SSE];
    
    if SSE<E0,break,end
    
    Delta2=2*lrate*Error.*NetworkO.*(1-NetworkO);
    Delta1=W2'*Delta2.*HO.*(1-HO);
    
    dW2Ex=Delta2*HOE';
    dW1Ex=Delta1*SIE';
    
    %采用动量BP学习算法 
   if i>1
       beta=0.5;%动量参数
       dW2Ex=beta*LdW2Ex+(1-beta)*dW2Ex;
       dW1Ex=beta*LdW1Ex+(1-beta)*dW1Ex;
       LdW2Ex=dW2Ex;
       LdW1Ex=dW1Ex;
   else
      LdW1Ex=dW1Ex;
      LdW2Ex=dW2Ex;
  end
    
    W1Ex=W1Ex+dW1Ex;
    W2Ex=W2Ex+dW2Ex;
 
    W2=W2Ex(:,1:HUN);
    
end

SSE;
NetworkO=logsig(W2Ex*HOE);

W1=W1Ex(:,1:InDim);
B1=W1Ex(:,InDim+1);
W2=W2Ex(:,1:HUN);
B2=W2Ex(:,1+HUN);

figure 
hold on
grid
[xx,Num]=size(ErrRecord);
plot(1:Num,ErrRecord,'k-');

%获取测试样本
TM11=M(1:20,2);TM12=M(51:70,2);TM13=M(101:120,2);
TM21=M(1:20,4);TM22=M(51:70,4);TM23=M(101:120,4);
%TM11=M(21:50,2);TM12=M(71:100,2);TM13=M(121:150,2);
%TM21=M(21:50,4);TM22=M(71:100,4);TM23=M(121:150,4);

TM1=[TM11' TM12' TM13']';
TM2=[TM21' TM22' TM23']';
TestSI=[TM1 TM2]';
[xx,TestSN]=size(TestSI);

%测试
TestHO=logsig(W1*TestSI+repmat(B1,1,TestSN));
TestNetworkO=logsig(W2*TestHO+repmat(B2,1,TestSN));
[Val,NNClass]=max(TestNetworkO);

for i=1:20
    TMO1(1,i)=1;
    TMO2(1,i)=2;
    TMO3(1,i)=3;
end
TestTargetO=[TMO1 TMO2 TMO3];

 NNC1Flag=abs(NNClass-1)<0.1;
 NNC2Flag=abs(NNClass-2)<0.1;
 NNC3Flag=abs(NNClass-3)<0.1;
 
 TargetC1Flag=abs(TestTargetO-1)<0.1;
 TargetC2Flag=abs(TestTargetO-2)<0.1;
 TargetC3Flag=abs(TestTargetO-3)<0.1;
 
 Test_C1_num=sum(NNC1Flag);
 Test_C2_num=sum(NNC2Flag);
 Test_C3_num=sum(NNC3Flag);
 
 Test_C1_C1=1.0*NNC1Flag * TargetC1Flag' %测试C1类，被正确分入C1类个数
 Test_C1_C2=1.0*NNC1Flag * TargetC2Flag' %错分至C2类个数 
 Test_C1_C3=1.0*NNC1Flag * TargetC3Flag' %错分至C3类个数 
 
 Test_C2_C1=1.0*NNC2Flag * TargetC1Flag'
 Test_C2_C2=1.0*NNC2Flag * TargetC2Flag'
 Test_C2_C3=1.0*NNC2Flag * TargetC3Flag'
    
 Test_C3_C1=1.0*NNC3Flag * TargetC1Flag'
 Test_C3_C2=1.0*NNC3Flag * TargetC2Flag'
 Test_C3_C3=1.0*NNC3Flag * TargetC3Flag'
 
 Test_Correct=(Test_C1_C1+Test_C2_C2+Test_C3_C3)/TestSN  %分类精度显示   
