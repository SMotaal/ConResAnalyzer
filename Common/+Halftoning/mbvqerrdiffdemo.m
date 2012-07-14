function mbvqerrdiffdemo(action,varargin);

% mbvqerrdiffdemo Minimum Brightness Variation Quadruple Error Diffusion Halftoning Demo
%                  
%   This Demo lets you design digital halftones by
%   Minimum Brightness Variation Quadruple Error Diffusion. 
%   Proposed by D. Shaked, N. Arad, A. Fitzhugh and I. Sobel
%   this method alters standard error diffusion by limiting the
%   number of ink colors used to render a specific pixel to minimize
%   the visibility of the halftone pattern. Error Diffusion is still
%   applied separably though to each color plane
%   The quality of the halftone is evaluated on the scale of various 
%   Image Quality Metrics.
%
%   Press the "Generate Halftone" button to get the desired error-diffused
%   halftone.   
%
%   The "Save Halftone" button saves the generated halftone to an image file. 
%   The halftone image file is saved by the name mbvq_errdiff_halftone.tif 
%   and may be read to a MATLAB variable using  the MATLAB function imread to 
%   get the matrix representation of the halftone.   
%                                       
%   Use the top menu to select from a number of possible images.
%   The next menu lets you choose the error filter for performing 
%   the halftoning. Choices are Floyd-Steinberg, Jarvis and Stucki
%   error filters.
%   
%   See also COLORERRORDIFF, DISPLAYCMY
%   For details on the Image Quality Metrics 
%   see  WSNR, PSNR, IMG_QI, LDM  

% Authored August 2002, Vishal Monga
% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.


% Function Subroutines:
%
% InitializeHalftoningDEMO   - sets up controls, axes, images, and initializes
%                              them. Calls LoadNewImage and CallFilter.
%
% SetUpMethod                - Sets up parameters for Halftoning
% 
% GenerateHalftone -         Generates the halftone with the specified parameters 
%
% LoadNewImage -        Loads a new image which is selected from the 
%                       popup
% 
% RadioUpdate -         Turns on the Radio Button which is clicked 
%                       on and turns the other radio buttons off.
%
% CallFilter -          Chooses which Error Filter to choose for Error Diffusion 
%                       and the scan type

if nargin<1,
   action='InitializeHALFTONINGDEMO1';
end;

feval(action,varargin{:}); %The function that takes the call backs
return;


%%%
%%%  Sub-function - InitializeMBVQ Error Diffusion Demo
%%%

function InitializeHALFTONINGDEMO1()

% If mbvqerrdiffdemo is already running, bring it to the foreground.
h = findobj(allchild(0), 'tag', 'Minimum Brightness Variation Quadruple Error Diffusion Halftoning');
if ~isempty(h)
   figure(h(1))
   return
end

% Do we have the needed Signal Processing Toolbox functions?
if (exist('remez','file') ~= 2)
    errString = {'HALFTONINGDEMO requires the Signal Processing Toolbox'};
    dlgName = 'HALFTONINGDEMODEMO error';
    errordlg(errString, dlgName);
    return
end

screenD = get(0, 'ScreenDepth');
if screenD>8
   grayres=256;
else
   grayres=128;
end
 
FirDemoFig = figure( ...
   'Name','Minimum Brightness Variation Quadruple Error Diffusion Halftoning', ...
   'NumberTitle','off', 'HandleVisibility', 'on', ...
   'tag', 'Minimum Brightness Variation Quadruple Error Diffusion Halftoning', ...
   'Visible','off', 'Resize', 'off',...
   'BusyAction','Queue','Interruptible','off', ...
   'IntegerHandle', 'off', ...
   'doublebuffer', 'on', ...
   'Colormap', gray(grayres));

figpos = get(FirDemoFig, 'position');  %Get handle to position of the window

% Adjust the size of the figure window
%figpos(3:4) = [560 420];
figpos(3:4) = [800 620];
horizDecorations = 10;  % resize controls, etc.
vertDecorations = 45;   % title bar, etc.
screenSize = get(0,'ScreenSize');
if (screenSize(3) <= 1)
    % No display connected (apparently)
    screenSize(3:4) = [100000 100000]; % don't use Inf because of vms
end
if (((figpos(3) + horizDecorations) > screenSize(3)) | ...
            ((figpos(4) + vertDecorations) > screenSize(4)))
    % Screen size is too small for this demo!
    delete(fig);
    error(['Screen resolution is too low ', ...
                '(or text fonts are too big) to run this demo']);
end
dx = screenSize(3) - figpos(1) - figpos(3) - horizDecorations;
dy = screenSize(4) - figpos(2) - figpos(4) - vertDecorations;
if (dx < 0)
    figpos(1) = max(5,figpos(1) + dx);
end
if (dy < 0)
    figpos(2) = max(5,figpos(2) + dy);
end
set(FirDemoFig, 'position', figpos);

%==================================
% Set up the image axes
row = figpos(4); col = figpos(3);  % dimensions of figure window

% The original image
%[Xsize Ysize] = size(get(hdl.OriginalImage,'Cdata'));
Xsize = 256; Ysize = 256; 

hdl.OrigImageAxes = axes('Parent', FirDemoFig, ...
   'Units','pixels', ...
   'BusyAction','Queue','Interruptible','off',...
   'ydir', 'reverse', ...
   'XLim', [.5 Xsize + 0.5], ...
   'YLim', [.5 Ysize + 0.5],...
   'CLim', [0 1], ...
   'Position',[50 320 Xsize Ysize], ...         %%%% Manipulate this entry to change the image position
   'XTick',[],'YTick',[]);
hdl.OriginalImage = image('Parent', hdl.OrigImageAxes,...
   'CData', [], ...
   'BusyAction','Queue','Interruptible','off',...
   'CDataMapping', 'scaled', ...
   'Xdata', [1 Xsize],...
   'Ydata', [1 Ysize],...
   'EraseMode', 'normal');   % should be none, HG geck 

% The Filtered Image
hdl.FiltImageAxes = axes('Parent', FirDemoFig, ...
   'Units','pixels', ...
   'BusyAction','Queue','Interruptible','off',...
   'ydir', 'reverse', ...
   'XLim', [.5 Xsize + 0.5], ...
   'YLim', [.5 Ysize + 0.5],...
   'CLim', [0 1], ...
   'Position',[320 320 Xsize Ysize], ...
   'XTick',[],'YTick',[]);
hdl.FilteredImage = image('Parent', hdl.FiltImageAxes,...
   'CData', [], ...
   'BusyAction','Queue','Interruptible','off',...
   'CDataMapping', 'scaled', ...
   'Xdata', [1 Xsize],...
   'Ydata', [1 Ysize],...
   'EraseMode', 'normal');   % should be none, HG geck

title('Halftone Image')

%%%%%%   Add Table for Quality Measures %%%%%%%%%%%%%%

yRef = 0.2;
grpleft = 0.09 + 0.1;

resWidth = 0.55;
resHeight = 0.12;
   % result frame setup
numColumn = 6;
numRow = 2;
border = 0.002;
tabWidth = (resWidth - (numColumn + 1) * border) / numColumn;
tabHeight = (resHeight - (numRow + 1) * border) / numRow;
resyPos = yRef+tabHeight+border;
% Save halftone Button
left = grpleft+ border + (border+tabWidth) - 0.005;
btnHt = 0.055; btnWid=0.23; txtsp = 0.043;
labelPos = [left yRef+0.25 btnWid txtsp];
labelStr = 'Save Halftone';
callbackStr='mbvqerrdiffdemo(''SaveHalftone'');';
hdl.SaveHalftone=uicontrol('Parent', FirDemoFig, ...
   'BusyAction','Queue','Interruptible','off',...
   'Style','pushbutton', ...
   'Units','normalized', ...
   'Position',labelPos, ...
   'Enable','on', ...
   'String',labelStr, ...
   'Callback',callbackStr);

gridNum = [1 1];
left = 0.1 + grpleft+ border + (border+tabWidth)*(gridNum(1)-1);
btnHt = 0.055; btnWid=0.185; txtsp = 0.043;
labelPos = [left-btnWid/2.5 0.12+yRef+btnHt*1.0 3*btnWid txtsp];
h = uicontrol('Parent', FirDemoFig, ...
   'Style','text', ...
   'Units','normalized', ...
   'Position',labelPos, ...
   'Horiz','left', ...
   'String','Figures of Merit (Luminance Channel)', ...
   'FontSize', 10, 'FontWeight', 'bold',...
   'Interruptible','off', ...
   'BackGroundColor',[0.8 0.8 0.8],...
   'ForegroundColor','black');

   % result table entries

   tabBottom = resyPos + border + (border+tabHeight)*(gridNum(2)-1);
   tabLeft = grpleft+ border + (border+tabWidth)*(gridNum(1)-1);
   tabPos = [tabLeft tabBottom tabWidth tabHeight];
   callBackStr = 'mbvqerrdiffdemo(''setUpMethod'')';
   hdl.h11 = uicontrol('Parent',FirDemoFig,...
      'Style','text', ...
      'Units','normalized', ...
      'Position',tabPos, ...
      'Horiz','center', ...
      'String',' ', ...
      'Interruptible','off', ...
      'BackgroundColor','white', ...
      'ForegroundColor','black',...
      'Callback',callBackStr);
   
   gridNum = [1 2];
   tabBottom = resyPos+border + (border+tabHeight)*(gridNum(2)-1);
   tabLeft = grpleft+ border + (border+tabWidth)*(gridNum(1)-1);
   tabPos = [tabLeft tabBottom tabWidth tabHeight];
   hdl.h12 = uicontrol('Parent',FirDemoFig,...
      'Style','text', ...
      'Units','normalized', ...
      'Position',tabPos, ...
      'Horiz','center', ...
      'String','PSNR(dB)', ...
      'Interruptible','off', ...
      'BackgroundColor','white', ...
      'ForegroundColor','black');
   
   gridNum = [2 1];
   tabBottom = resyPos+border + (border+tabHeight)*(gridNum(2)-1);
   tabLeft = grpleft+ border + (border+tabWidth)*(gridNum(1)-1);
   tabPos = [tabLeft tabBottom tabWidth tabHeight];
   callBackStr = 'mbvqerrdiffdemo(''setUpMethod'')'; 
   hdl.h21 = uicontrol('Parent',FirDemoFig,...
      'Style','text', ...
      'Units','normalized', ...
      'Position',tabPos, ...
      'Horiz','center', ...
      'String','', ...
      'Interruptible','off', ...
      'BackgroundColor','white', ...
      'ForegroundColor','black',...
      'Callback',callBackStr);
   
   gridNum = [2 2];
   tabBottom = resyPos+border + (border+tabHeight)*(gridNum(2)-1);
   tabLeft = grpleft+ border + (border+tabWidth)*(gridNum(1)-1);
   tabPos = [tabLeft tabBottom tabWidth tabHeight];
   hdl.h22 = uicontrol('Parent',FirDemoFig,...
      'Style','text', ...
      'Units','normalized', ...
      'Position',tabPos, ...
      'Horiz','center', ...
      'String','WSNR(dB)', ...
      'Interruptible','off', ...
      'BackgroundColor','white', ...
      'ForegroundColor','black');
   
   gridNum = [3 1];
   tabBottom = resyPos+border + (border+tabHeight)*(gridNum(2)-1);
   tabLeft = grpleft+ border + (border+tabWidth)*(gridNum(1)-1);
   tabPos = [tabLeft tabBottom tabWidth tabHeight];
   callBackStr = 'mbvqerrdiffdemo(''setUpMethod'')';
   hdl.h31 = uicontrol('Parent',FirDemoFig,...
      'Style','text', ...
      'Units','normalized', ...
      'Position',tabPos, ...
      'Horiz','center', ...
      'String','', ...
      'Interruptible','off', ...
      'BackgroundColor','white', ...
      'ForegroundColor','black',...
      'Callback',callBackStr);
   
   gridNum = [3 2];
   tabBottom = resyPos+border + (border+tabHeight)*(gridNum(2)-1);
   tabLeft = grpleft+ border + (border+tabWidth)*(gridNum(1)-1);
   tabPos = [tabLeft tabBottom tabWidth tabHeight];
   hdl.h32 = uicontrol( 'Parent',FirDemoFig,...
      'Style','text', ...
      'Units','normalized', ...
      'Position',tabPos, ...
      'Horiz','center', ...
      'String','LDM', ...
      'Interruptible','off', ...
      'BackgroundColor','white', ...
      'ForegroundColor','black');
   
   gridNum = [4 1];
   tabBottom = resyPos+border + (border+tabHeight)*(gridNum(2)-1);
   tabLeft = grpleft+ border + (border+tabWidth)*(gridNum(1)-1);
   tabPos = [tabLeft tabBottom tabWidth tabHeight];
   callBackStr = 'mbvqerrdiffdemo(''setUpMethod'')';
   hdl.h41 = uicontrol('Parent',FirDemoFig,...
      'Style','text', ...
      'Units','normalized', ...
      'Position',tabPos, ...
      'Horiz','center', ...
      'String','', ...
      'Interruptible','off', ...
      'BackgroundColor','white', ...
      'ForegroundColor','black',...
      'Callback',callBackStr);   
  
   gridNum = [4 2];
   tabBottom = resyPos+border + (border+tabHeight)*(gridNum(2)-1);
   tabLeft = grpleft+ border + (border+tabWidth)*(gridNum(1)-1);
   tabPos = [tabLeft tabBottom tabWidth tabHeight];
   hdl.h42 = uicontrol('Parent',FirDemoFig,...
      'Style','text', ...
      'Units','normalized', ...
      'Position',tabPos, ...
      'Horiz','center', ...
      'String','UQI', ...
      'Interruptible','off', ...
      'BackgroundColor','white', ...
      'ForegroundColor','black');
   
   
%====================================
% Information for all buttons (and menus)
bgc = [0.45 0.45 0.45];
c = get(FirDemoFig,'Color');
labelColor=[0.8 0.8 0.8];
yInitPos=0.90;
menutop=0.95;
btnTop = 0.6;
top=0.75;
left=0.785;
btnWid=0.185;
btnHt=0.055;
textHeight = 0.05;
textWidth = 0.07;
% Spacing between the button and the next command's label
spacing=0.018;
txtsp=.043;   % This can be changed

%====================================
% The CONSOLE frame
frmBorder=0.019; frmBottom=0.04; 
frmHeight = 0.92; frmWidth = btnWid;
yPos=frmBottom-frmBorder;
frmPos=[left-frmBorder yPos frmWidth+2*frmBorder frmHeight+2*frmBorder];
h=uicontrol( 'Parent', FirDemoFig, ...
   'Style','frame', ...
   'Units','normalized', ...
   'Position',frmPos, ...
   'BackgroundColor',bgc);

%====================================
% The LoadNewImage menu
menuNumber=1;
yPos=menutop-(menuNumber-1)*(btnHt+spacing)-.7*txtsp;

btnPos=[left yPos-btnHt btnWid btnHt];
hdl.ImgPop=uicontrol('Parent', FirDemoFig, ...
   'BusyAction','Queue','Interruptible','off',...
   'Style','popupmenu', ...
   'Units','normalized', ...
   'Position',btnPos, ...
   'Enable','on', ...
   'String','Lena|Peppers|House|SailBoat|Trees|Girl', ...
   'Tag','ImagesPop',...
   'Callback','mbvqerrdiffdemo(''LoadNewImage'')');
labelPos = [left btnPos(2)+btnHt*1.0 btnWid txtsp];
h = uicontrol('Parent', FirDemoFig, ...
   'Style','text', ...
   'Units','normalized', ...
   'Position',labelPos, ...
   'Horiz','left', ...
   'String','Select an Image:', ...
   'Interruptible','off', ...
   'BackgroundColor',bgc, ...
   'ForegroundColor','white');

%====================================
% The FilterType Menu
menuNumber=2;
yPos=menutop-(menuNumber-1)*(btnHt+spacing)-1.4*txtsp;
labelStr='Floyd-Steinberg|Jarvis|Stucki';
callbackStr='mbvqerrdiffdemo(''setUpMethod'');';

btnPos=[left yPos-btnHt btnWid btnHt];
hdl.FiltType = uicontrol('Parent', FirDemoFig, ...
   'BusyAction','Queue','Interruptible','off',...
   'Style','popupmenu', ...
   'Units','normalized', ...
   'Position',btnPos, ...
   'String',labelStr, ...
   'Interruptible','on', ...
   'Callback',callbackStr);
labelPos = [left btnPos(2)+btnHt*1.0 btnWid txtsp];
h = uicontrol('Parent', FirDemoFig, ...
   'Style','text', ...
   'Units','normalized', ...
   'Position',labelPos, ...
   'Horiz','left', ...
   'String','Choose Error Filter:', ...
   'Interruptible','off', ...
   'BackgroundColor',bgc, ...
   'ForegroundColor','white');


 % Label for Scan Type
 yPos = yPos - 0.1;
 labelPos = [left yPos btnWid txtsp];
 radioHt = btnHt*.9;
%====================================
btnTop = yPos;
%====================================
btnNumber=2;
%====================================
% Apply Filter Frame
framePos=[0.17+5*spacing spacing+(yRef-0.1) btnWid+2*spacing btnHt+2*spacing];
h=uicontrol( 'Parent', FirDemoFig, ...
   'Style','frame', ...
   'Units','normalized', ...
   'Position',framePos, ...
   'BackgroundColor',bgc);

%====================================
% The Generate Halftone button
labelStr='Generate Halftone';
callbackStr='mbvqerrdiffdemo(''GenerateHalftone'')';
btnPos=[framePos(1)+spacing framePos(2)+spacing btnWid btnHt];
hdl.Apply=uicontrol('Parent', FirDemoFig, ...
   'BusyAction','Queue','Interruptible','off',...
   'Style','pushbutton', ...
   'Units','normalized', ...
   'Position',btnPos, ...
   'Enable','off', ...
   'String',labelStr, ...
   'Callback',callbackStr);

%====================================
% The INFO button
labelStr='Info';
callbackStr='helpwin mbvqerrdiffdemo';
hdl.Help=uicontrol('Parent', FirDemoFig, ...
   'BusyAction','Queue','Interruptible','off',...
   'Style','pushbutton', ...
   'Units','normalized', ...
   'Position',[left frmBottom+btnHt+spacing btnWid btnHt], ...
   'String',labelStr, ...
   'Enable','off', ...
   'Callback',callbackStr);

%====================================
% The CLOSE button
labelStr='Close';
callbackStr='close(gcbf)';
hdl.Close=uicontrol('Parent', FirDemoFig, ...
   'BusyAction','Queue','Interruptible','off',...
   'Style','pushbutton', ...
   'Units','normalized', ...
   'Position',[left frmBottom btnWid btnHt], ...
   'String',labelStr, ...
   'Enable','off', ...
   'Callback',callbackStr);

%====================================
% Status bar
hdl.Status = uicontrol( ...
   'Parent', FirDemoFig, ...
   'BusyAction','Queue','Interruptible','off',...
   'Style','text', ...
   'Units','pixels', ...
   'Position',[190 10 230 18], ...
   'Foreground', [.8 0 0], ...
   'Background',c, ...
   'Horiz','center', ...
   'Tag', 'Status', ...
   'String','Initializing Demo...');

set(FirDemoFig, 'Userdata', hdl, 'Visible', 'on');

% rotate3d ON;
LoadNewImage(FirDemoFig);
setUpMethod(FirDemoFig);
GenerateHalftone(FirDemoFig);
set(FirDemoFig,'HandleVisibility','Callback');
set([hdl.Help hdl.Close] , 'enable', 'on');
return

%%%
%%%  Sub-Function - RadioUPDATE
%%%

function RadioUPDATE(s)
DemoFig = gcbf;
hdl=get(DemoFig,'Userdata');
set([hdl.Btn1 hdl.Btn2], 'value', 0);
switch s %  Enable selected button
case 1
   set(hdl.Btn1,'value',1)
case 2
   set(hdl.Btn2,'value',1) 
end

set(hdl.Btn1,'Userdata',s) 
setUpScan(DemoFig);
return


%%%%%%%%%%% SubFunction to Call Appropriate Filter %%%%%%%%%%%%

function OUT = CallFilter(DemoFig)

if nargin < 1
   DemoFig = gcbf;
end
if( ~(exist('curr') == 1)) 
    curr = 0;
end    

setstatus(DemoFig, '');
set(gcf,'Pointer','watch');
hdl = get(DemoFig,'Userdata');

inp = get(hdl.OriginalImage,'CData');

filterChoice = get(hdl.FiltType,'value'); 

% Extract Color Planes from the image
cyanIm = 1 - inp(:,:,1);
magentaIm = 1 - inp(:,:,2);
yellowIm = 1 - inp(:,:,3);

% Condition for Error Diffusion into RGB planes
redIm = 1 - cyanIm;
greenIm = 1 - magentaIm;
blueIm = 1 - yellowIm;



switch filterChoice
case 1
       FS_3 =	[0 0 7;  ...
	             3 5 1];
       FS_3 = FS_3 / sum(FS_3(:));
       OUT = ColorErrorDiff(FS_3, redIm, greenIm, blueIm);
case 2
       FS_3 = [0 0 0 7 5; 3 5 7 5 3; 1 3 5 3 1]/48;
       OUT = ColorErrorDiff(FS_3, redIm, greenIm, blueIm);
case 3
       FS_3 = [0 0 0 8 4; 2 4 8 4 2; 1 2 4 2 1]/42;
       OUT = ColorErrorDiff(FS_3, redIm, greenIm, blueIm);
end
   
set(DemoFig,'Pointer','arrow')

drawnow
setstatus(DemoFig, '');
set(hdl.Apply, 'Enable', 'on')

return

%%
%%

function setUpMethod(DemoFig)

if nargin<1
   DemoFig = gcbf;
end

setstatus(DemoFig, '');
set(gcf,'Pointer','watch');
hdl = get(DemoFig,'Userdata');

set(DemoFig,'Pointer','arrow');

drawnow
setstatus(DemoFig, '');
set(hdl.Apply, 'Enable', 'on')
return


%%
%% Sub - Function set up ALL the stuff
%%

function setUpScan(DemoFig)

if nargin<1
   DemoFig = gcbf;
end

setstatus(DemoFig, '');
set(gcf,'Pointer','watch');
hdl = get(DemoFig,'Userdata');

set(DemoFig,'Pointer','arrow');

drawnow
setstatus(DemoFig, '');
set(hdl.Apply, 'Enable', 'on')
return

%%%
%%%  Sub-Function - SaveHalftone
%%%

function SaveHalftone(DemoFig)

if nargin<1
   DemoFig = gcbf;
end

setstatus(DemoFig, '');
set(DemoFig,'Pointer','watch');
hdl = get(DemoFig,'Userdata');

mbvq_errdiff_halftone = get(hdl.FilteredImage,'Cdata');
imwrite(mbvq_errdiff_halftone,'mbvq_errdiff_halftone.tif');

set(DemoFig,'Pointer','arrow');
set(hdl.SaveHalftone, 'Enable', 'off')
drawnow
setstatus(DemoFig, '');
return;



%%%
%%%  Sub-Function - GenerateHalftone
%%%

function GenerateHalftone(DemoFig)

if nargin<1
   DemoFig = gcbf;
end

setstatus(DemoFig, '');
set(DemoFig,'Pointer','watch');
hdl=get(DemoFig,'Userdata');

I = getimage(hdl.OriginalImage);
ORIG = 255*get(hdl.OriginalImage,'Cdata');
% Extract Color Planes from the image
cyanIm = 1 - ORIG(:,:,1)/255;
magentaIm = 1 - ORIG(:,:,2)/255;
yellowIm = 1 - ORIG(:,:,3)/255;

% Condition for Error Diffusion into RGB planes
redIm = 1 - cyanIm;
greenIm = 1 - magentaIm;
blueIm = 1 - yellowIm;

% Obtain output halftone image
J = CallFilter(DemoFig);

cyanout = 1 - J(:,:,1);
magentaout = 1 - J(:,:,2);
yellowout = 1 - J(:,:,3);

% Condition for Error Diffusion into RGB planes
redout = 1 - cyanout;
greenout = 1 - magentaout;
blueout = 1 - yellowout;

%%%%%%%%%%%% Get Luminance Representations of Images to apply Figures of Merit %%%%%%%%%%

col_lum_orig = rgb2gray(ORIG);
halftone_lum = rgb2gray(J*255);
half_lum = double(rgb2gray(uint8(J*255)));

% Calculate the 4 Figures of Merit right here 
measure_psnr = psnr(col_lum_orig,half_lum) + 1;
measure_wsnr = wsnr(col_lum_orig,halftone_lum);
measure_ldm = ldm(col_lum_orig,halftone_lum);
measure_uqi = img_qi(col_lum_orig,halftone_lum);


% Set those values in the Quality Measure table
set(hdl.h11,'String',measure_psnr');
set(hdl.h21,'String',measure_wsnr');
set(hdl.h31,'String',measure_ldm');
set(hdl.h41,'String',measure_uqi');

% Get Pointer and Enable Generate Halftone Button
set(hdl.FilteredImage, 'Cdata', J);
set(DemoFig,'Pointer','arrow');
set(hdl.Apply, 'Enable', 'off')
set(hdl.SaveHalftone, 'Enable', 'on')
drawnow
setstatus(DemoFig, '');
return


%%%
%%%  Sub-Function - LoadNewImage
%%%

function LoadNewImage(DemoFig)

if nargin<1
   callb = 1;    % We're in a callback
   DemoFig = gcbf;
else 
   callb = 0;    % We're in the initialization
end

set(DemoFig,'Pointer','watch');
hdl=get(DemoFig,'Userdata');
v = get(hdl.ImgPop,{'value','String'});
name = deblank(v{2}(v{1},:));
setstatus(DemoFig, ['']);
drawnow

switch name
case 'Lena'
   lena = imread('lenacolor.tif');
   img = double(lena);
case 'Peppers'
   peppers = imread('pepperscolor.tif');
   img = double(peppers);
case 'House',
   barbara = imread('housecolor.tif');
   img = double(barbara);
case 'SailBoat',
   boat = imread('boatcolor.tif');
   img = double(boat);
case 'Trees'
   trees = imread('treescolor.tif');
   img = double(trees);
case 'Girl'
   clock = imread('girlcolor.tif');
   img = double(clock);   
   
otherwise 
   error('MBVQ Error Diffusion DEMO: Unknown Image Option!');
end

img = double(img)/255;
set(hdl.OriginalImage, 'Cdata', img);
set(get(hdl.OrigImageAxes,'title'),'string',['Original Image']);

drawnow
set(DemoFig,'Pointer','arrow')
if callb
   GenerateHalftone(DemoFig); 
end
drawnow
return;

