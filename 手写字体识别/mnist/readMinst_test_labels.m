clear all;
clc;
%读取训练图片标签数据文件
[FileName1,PathName1] = uigetfile('*.*','选择测试图片数据文件t10k-labels.idx1-ubyte');
InFile = fullfile(PathName1,FileName1);
fid1 = fopen(InFile,'r');
a = fread(fid1,8,'uint8');
MagicNum = ((a(1)*256+a(2))*256+a(3))*256+a(4);
ImageNum = ((a(5)*256+a(6))*256+a(7))*256+a(8);
if ((MagicNum~=2049)||(ImageNum~=10000))
    error('不是 MNIST t10k-labels.idx1-ubyte 文件！');
    fclose(fid1);    
    return;    
end
h_w = waitbar(0,'请稍候，处理中>>');
mnist_test_labels=zeros(ImageNum,10);
for i=1:ImageNum
   b= fread(fid1,1,'uint8');  
   pos=mod(b,10);
   if (pos==0)
       pos=pos+10;
   end
   mnist_test_labels(i,pos)=1;
   waitbar(i/ImageNum);
end
save mnist_test_labels.mat  mnist_test_labels
fclose(fid1);
close(h_w);