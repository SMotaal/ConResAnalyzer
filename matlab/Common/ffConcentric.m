function [ p1 ] = ffConcentric( cycles, mean, contrast, width )
%FFCONCENTRIC Summary of this function goes here
%   Detailed explanation goes here

global hp hf;

fftx = @(x) fftshift(fft2(x));
fftd = @(x) abs(log(1 + abs(x))./10);
fftr = @(x) fftd(fftx(x));

iftx = @(x) ifft2(ifftshift(x));

scrn = @(x) screen_19c(x);
rsz     = 0.9;
scl     = 1.0; % 0.5

midrg   = @(x) 1:round(size(x,2)/2);
combimg = @(x, y) [x y];

if nargin<1, cycles   =  10.0; end

if ~isscalar(cycles), 
  src   = im2double(cycles);
  
  imgs  = size(src);
  imgh  = imgs(1);
  imgw  = imgs(2);
  
  width = ceil(max(imgs)/2)*2;
  
  if imgh ~= imgw || imgw~=width
    img = zeros(width, width);
    
    rgw = round(width-imgw):round(width-imgw)+imgw;
    rgh = round(width-imgh):round(width-imgh)+imgh;
    
    img(rgh, rgw) = src;
  else
    img = src;
  end
else
  if nargin<2, mean     =    50; end
  if nargin<3, contrast =   100; end
  if nargin<4, width    =   128; end
  
  width = 2^nextpow2(width);

  img = Grasppe.Patterns.ConcentricCircles(cycles, mean, contrast, width);
end

rg      = 1+width/2:width*1.5;

img0    = zeros(2*width, 2*width);
img0(rg, rg) = img;

msk     = ceil(img0);

img1    = imresize(scrn(imresize(img0,rsz)), size(img0));
fti1    = fftx(img1);
ift1    = abs(iftx(fti1));
fim1    = fftd(fti1);

imgs    = msk.*0.5;
imgs    = imresize(scrn(imresize(imgs,rsz)), size(img0));
ftis    = fftx(imgs);
ifts    = abs(iftx(ftis));
fims    = fftd(ftis);


%H = bandfilter('gaussian', 'pass', width, width, cycles*2, width/cycles/2);
H = lpfilter('gaussian', width, width, cycles*8);
imgh = zeros(2*width, 2*width);
imgh(rg, rg) = fftshift(H);

flt=imgh; 
% flt = 1-(im2bw(real(ftis)).*msk); % 1-im2bw(real(ftis));
ftr=fti1.*flt; %imshow([img1 fti1 flt ftr iftx(ftr)]);

%img2    = imgs;
img2    = abs(iftx(ftr)).*msk;
img2(msk==0) = 1;
fti2    = fftx(img2);
ift2    = iftx(fti2);
fim2    = fftd(fti2);

dim1    = imadjust(fim1); dim1(:,midrg(img1)) = img1(:,midrg(img1));
dim1    = [dim1(rg, rg) imadjust(ift1(rg, rg))];
dim2    = imadjust(fim2); dim2(:,midrg(img2)) = img2(:,midrg(img2));
dim2    = [dim2(rg, rg) imadjust(ift2(rg, rg))];

dim3    = [dim1', dim2']';

try
  if isequal(ishghandle(hp), true)
      api = iptgetapi(hp);  
      api.replaceImage(dim3);
      api.setMagnification(scl);
      figure(hf);
  else
    error('Not a handle');
  end
catch err
  hf      = figure('WindowStyle', 'docked');
  hp      = imdisp(dim3, hf, scl); % , hf, 0.5);
end

p1 = hp;

commandwindow;
end


% function packimg(x, y, rx, ry)
% end



% img = Grasppe.Patterns.ConcentricCircles(10, 50, 100);
% p1 = imdisp(img,figure(1));
% p1 = imdisp(real(fft2(img)),figure(2));
% imgf = abs(fft2(img));
% p1 = imdisp(abs(fft2(img)),figure(2));
% p1 = imshow(abs(fft2(img)),[]);
% p1 = imshow(abs(fft2(img.*255)),[]);
% p1 = imshow(abs(fft2(round(img.*255))),[]);
% p1 = imshow(abs(fftshift(fft2(img))),[]);
% p1 = imshow(log + (1 * abs(fftshift(fft2(img)))),[]);
% p1 = imshow(log(1 + abs(fftshift(fft2(img)))),[]);
% p1 = imdisp(log(1 + abs(fftshift(fft2(img)))));
% p1 = imdisp(log(1 + abs(fftshift(fft2(img)))), figure(2));
% log(1 + abs(fftshift(fft2(img))
% s = log(1 + abs(fftshift(fft2(img))))
% p1 = imdisp(log(1 + abs(fftshift(fft2(img))))./ 10, figure(2));
% s = log(1 + abs(fftshift(fft2(img))))./10
% imshow(s)
% p1 = imdisp(s, figure(2));
% figure(s), imshow(s)
% figure(2), imshow(s)
