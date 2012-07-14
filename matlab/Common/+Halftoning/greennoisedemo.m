function greennoisedemo(action,varargin);

% GREENNOISEDEMO Green Noise Error Diffusion Halftoning Demo
%                  
%   This Demo lets you design grayscale digital halftones by
%   Green Noise Error Diffusion. The quality of the halftone is  
%   evaluated on the scale of various Image Quality Metrics.
%
%   Press the "Generate Halftone" button to get the desired error-diffused
%   halftone.   
%                  
%   The "Save Halftone" button saves the generated halftone to an image file. 
%   The halftone image file is saved by the name green_halftone.tif 
%   and may be read to a MATLAB variable using  the MATLAB function imread to 
%   get the matrix representation of the halftone.   
%
%   Use the top menu to select from a number of possible images.
%   The next menu lets you choose between traditional Green Noise Halftones
%   with a fixed Hysteresis filter or use an algorithm to adapt the Hysteresis Filter.
%   The Hysteresis constant G holds constant the dot size. The error filter is set to Stucki
%   but may be changed by making changes to GREEN_ADAP, GREEN_FIXED.
%   You may further select your choice of scan - Raster/Serpentine
%                                         
%   See also GREEN_FIXED, GREEN_ADAP
%   For details on the Image Quality Metrics 
%   see  WSNR, PSNR, IMG_QI, LDM  

% Authored July 2002 by Vishal Monga

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
feval(action,varargin{:});

return;


%%%
%%%  Sub-function - InitializeFIRDEMO
%%%

function InitializeHALFTONINGDEMO1()

% If greennoisedemo is already running, bring it to the foreground.
h = findobj(allchild(0), 'tag', 'Green Noise Error Diffusion Halftoning');
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
   'Name','Green Noise Error Diffusion Halftoning', ...
   'NumberTitle','off', 'HandleVisibility', 'on', ...
   'tag', 'Green Noise Error Diffusion Halftoning', ...
   'Visible','off', 'Resize', 'off',...
   'BusyAction','Queue','Interruptible','off', ...
   'IntegerHandle', 'off', ...
   'doublebuffer', 'on', ...
   'Colormap', gray(grayres));

figpos = get(FirDemoFig, 'position');  %Get handle to position of the window

% Adjust the size of the figure window
%figpos(3:4) = [560 420];
figpos(3:4) = [820 620];
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

%%%%%%   Add Table for Figures of Merit %%%%%%%%%%%%%%

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

left = grpleft+ border + (border+tabWidth) - 0.005;
btnHt = 0.055; btnWid=0.23; txtsp = 0.043;
labelPos = [left yRef+0.25 btnWid txtsp];
labelStr = 'Save Halftone';
callbackStr='greennoisedemo(''SaveHalftone'');';
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
btnHt = 0.055; btnWid=0.23; txtsp = 0.043;
labelPos = [left 0.12+yRef+btnHt*1.0 btnWid txtsp];
h = uicontrol('Parent', FirDemoFig, ...
   'Style','text', ...
   'Units','normalized', ...
   'Position',labelPos, ...
   'Horiz','left', ...
   'String','Figures of Merit', ...
   'FontSize', 12, 'FontWeight', 'bold',...
   'Interruptible','off', ...
   'BackGroundColor',[0.8 0.8 0.8],...
   'ForegroundColor','black');

   % result table entries

   tabBottom = resyPos + border + (border+tabHeight)*(gridNum(2)-1);
   tabLeft = grpleft+ border + (border+tabWidth)*(gridNum(1)-1);
   tabPos = [tabLeft tabBottom tabWidth tabHeight];
   callBackStr = 'greennoisedemo(''setUpMethod'');';
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
   callBackStr = 'greennoisedemo(''setUpMethod'');'; 
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
   callBackStr = 'greennoisedemo(''setUpMethod'');';
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
   callBackStr = 'greennoisedemo(''setUpMethod'');';
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
   
   
% The Axes for plotting filter coefficients
% hdl.Filter = axes('Parent', FirDemoFig, ...
%    'Units','pixels', ...
%    'Position', [50 74 128 128]);
% title('Filter Coefficients');
% 
% % The Axes for plotting filter frequency response
% hdl.Response = axes('Parent', FirDemoFig, ...
%    'Units','pixels', ...
%    'Position', [240 74 128 128]);
% title('Frequency Response');

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
btnWid=0.23;
btnHt=0.055;
textHeight = 0.05;
textWidth = 0.185;
% Spacing between the button and the next command's label
spacing=0.018;
txtsp=.043;   % This can be changed

%====================================
% The CONSOLE frame
frmBorder=0.019; frmBottom=0.04; 
frmHeight = 0.92; frmWidth = btnWid;
yPos=frmBottom-frmBorder;
frmPos=[left-3*frmBorder yPos frmWidth+2*frmBorder frmHeight+2*frmBorder];
h=uicontrol( 'Parent', FirDemoFig, ...
   'Style','frame', ...
   'Units','normalized', ...
   'Position',frmPos, ...
   'BackgroundColor',bgc);

%====================================
% The LoadNewImage menu
menuNumber=1;
yPos=menutop-(menuNumber-1)*(btnHt+spacing)-.7*txtsp;
left = left - 2*frmBorder;
btnPos=[left yPos-btnHt btnWid btnHt];
hdl.ImgPop=uicontrol('Parent', FirDemoFig, ...
   'BusyAction','Queue','Interruptible','off',...
   'Style','popupmenu', ...
   'Units','normalized', ...
   'Position',btnPos, ...
   'Enable','on', ...
   'String','Lena|Peppers|Barbara|Boat|Trees|Clock', ...
   'Tag','ImagesPop',...
   'Callback','greennoisedemo(''LoadNewImage'');');
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
% The EdgeEnhancement Error Diffusion Menu
menuNumber=2;
yPos=menutop-(menuNumber-1)*(btnHt+spacing)-1.4*txtsp;
labelStr='Fixed Hysteresis Filter|Adapt Hysteresis Filter';
callbackStr='greennoisedemo(''setUpEdge'');';

btnPos=[left yPos-btnHt btnWid btnHt];
hdl.EdgeType = uicontrol('Parent', FirDemoFig, ...
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
   'String','Choose algorithm for Green Noise error diffusion', ...
   'Interruptible','off', ...
   'BackgroundColor',bgc, ...
   'ForegroundColor','white');

%====================================
% The Initial Guess Menu
menuNumber=3.6;
yPos=menutop-(menuNumber-1)*(btnHt+spacing)-1.4*txtsp;
labelStr='Floyd-Steinberg|User Defined';
callbackStr='greennoisedemo(''setUpEdge'');';

btnPos=[left yPos-btnHt btnWid btnHt];
hdl.GuessMenu = uicontrol('Parent', FirDemoFig, ...
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
   'String','Hysteresis Filter (for Adaptive error diffusion this serves as the initial guess)', ...
   'Interruptible','off', ...
   'BackgroundColor',bgc, ...
   'ForegroundColor','white');

%===================================
% Label for the slider
yPos = yPos - 1.8*btnHt;
labelPos = [left yPos btnWid txtsp];
h = uicontrol('Parent', FirDemoFig, ...
   'Style','text', ...
   'Units','normalized', ...
   'Position',labelPos, ...
   'Horiz','left', ...
   'String','Select Hysteresis Constant G', ...
   'Interruptible','off', ...
   'BackgroundColor',bgc, ...
   'ForegroundColor','white');

% Set up scroll bar
barHeight = 0.04; barWidth = btnWid; %*frmBorder;
sbBottom=yPos-1.0*barHeight;
sbLeft = left;
textWidth = barWidth/2;
scrollPos = [sbLeft sbBottom barWidth barHeight];
callbackStr = 'greennoisedemo(''setUpMethod'');';
hdl.Slider = uicontrol( ...
   'Parent', FirDemoFig, ...
   'BusyAction','Queue','Interruptible','off',...
   'Style','slider', ...
   'Units','normalized', ...
   'Position',scrollPos, ...
   'Value',0.5, ...
   'min',0, ...
   'max',1, ...
   'Interruptible','off', ...
   'Callback',callbackStr);
% Left and right range indicators
c = get(FirDemoFig,'Color');
if [.298936021 .58704307445 .114020904255]*c'<.5,
   fgColor = [1 1 1];
else
   fgColor = [0 0 0];
end
rangePos = [sbLeft sbBottom-barHeight textWidth barHeight];
uicontrol( ...
   'Parent', FirDemoFig, ...
   'BusyAction','Queue','Interruptible','off',...
   'Style','text', ...
   'Units','normalized', ...
   'Position',rangePos, ...
   'Horiz','left', ...
   'Background',bgc, ...
   'Foreground',fgColor, ...
   'String','0');
rangePos = [sbLeft+barWidth/2 sbBottom-barHeight textWidth barHeight];
uicontrol( ...
   'Parent', FirDemoFig, ...
   'BusyAction','Queue','Interruptible','off',...
   'Style','text', ...
   'Units','normalized', ...
   'Position',rangePos, ...
   'Horiz','right', ...
   'Background',bgc, ...
   'Foreground',fgColor, ...
   'String','1');

%===================================
% textBox for Initial Guess to the Hysteresis Filter

yPos = yPos - .1; 
labelWidth = frmWidth-textWidth-.01;
labelLeft = left;
labelPos = [left yPos  2*textWidth txtsp];
 h = uicontrol('Parent', FirDemoFig, ...
    'Style','text', ...
   'Units','normalized', ...
   'Position',labelPos, ...
   'Horiz','left', ...
   'String','Enter Initial Guess for the Hysteresis Filter (2 x 3 and identify center)', ...
   'Interruptible','off', ...
   'BackgroundColor',bgc, ...
   'ForegroundColor','white');
% Text field
labelBottom = yPos - textHeight;
textPos = [labelLeft labelBottom 1.8*textWidth textHeight];
callbackStr = 'greennoisedemo(''textHysteresis'');';
hdl.Hysteresis = uicontrol('Parent', FirDemoFig, ...
   'BusyAction','Queue','Interruptible','off',...
   'Style','edit', ...
   'Units','normalized', ...
   'Position',textPos, ...
   'Horiz','right', ...
   'Background','white', ...
   'Foreground','black', ...
   'String','[0 0.5 0;0.5 -99 0]','Userdata',[0 0.5 0;0.5 -99 0], ...
   'callback',callbackStr);

% %====================================
 % Label for Scan Type
 yPos = yPos - 0.1;
 labelPos = [left yPos btnWid txtsp];
 h = uicontrol('Parent', FirDemoFig, ...
    'Style','text', ...
   'Units','normalized', ...
   'Position',labelPos, ...
   'Horiz','left', ...
   'String','Scan Type', ...
   'Interruptible','off', ...
   'BackgroundColor',bgc, ...
   'ForegroundColor','white');

radioHt = btnHt*.9;
%====================================
% RASTER radio button
btnTop = yPos;
btnNumber=1;
yPos = btnTop-(btnNumber-1)*(radioHt+spacing);
labelStr='Raster';
callbackStr='greennoisedemo(''RadioUPDATE'',1);';

% Generic button information
btnPos=[left yPos-radioHt btnWid radioHt];
hdl.Scan1=uicontrol('Parent', FirDemoFig, ...
   'BusyAction','Queue','Interruptible','off',...
   'Style','radiobutton', ...
   'Units','normalized', ...
   'Position',btnPos, ...
   'String',labelStr, ...
   'value',1,'Userdata',1, ...
   'Callback',callbackStr);

%====================================
% SERPENTINE radio button
btnNumber=2;
yPos = btnTop-(btnNumber-1)*(radioHt+spacing);
labelStr='Serpentine';
callbackStr='greennoisedemo(''RadioUPDATE'',2);';

% Generic button information
btnPos=[left yPos-radioHt btnWid radioHt];
hdl.Scan2 = uicontrol('Parent', FirDemoFig, ...
   'BusyAction','Queue','Interruptible','off',...
   'Style','radiobutton', ...
   'Units','normalized', ...
   'Position',btnPos, ...
   'String',labelStr, ...
   'value',0, ...
   'Callback',callbackStr);




%========================================
% The Window Menu
% menuNumber=3;
% yPos=menutop-(menuNumber-1)*(btnHt+spacing)-2.1*txtsp
% labelStr=str2mat('Bartlett','Hamming','Hanning','Blackman');
% callbackStr='greennoisedemo(''DesignFilter'')';
% 
% % Generic button information
% btnPos=[left yPos-btnHt btnWid btnHt];
% hdl.Window=uicontrol('Parent', FirDemoFig, ...
%    'BusyAction','Queue','Interruptible','off',...
%    'Style','popupmenu', ...
%    'Units','normalized', ...
%    'Position',btnPos, ...
%    'String',labelStr, ...
%    'Interruptible','on', ...
%    'Enable', 'off', ...
%    'Callback',callbackStr);
% labelPos = [left btnPos(2)+btnHt*1.0 btnWid txtsp];
% h = uicontrol('Parent', FirDemoFig, ...
%    'Style','text', ...
%    'Units','normalized', ...
%    'Position',labelPos, ...
%    'Horiz','left', ...
%    'String','Window method:', ...
%    'Interruptible','off', ...
%    'BackgroundColor',bgc, ...
%    'ForegroundColor','white');
% 
% %===================================
% % Cutoff label and text field
% top = yPos - btnHt - spacing;
% labelWidth = frmWidth-textWidth-.01;
% labelBottom=top-textHeight;
% labelLeft = left;
% labelPos = [labelLeft labelBottom labelWidth textHeight]
% h = uicontrol('Parent', FirDemoFig, ...
%    'Style','text', ...
%    'Units','normalized', ...
%    'Position',labelPos, ...
%    'Horiz','left', ...
%    'String','Cutoff:', ...
%    'Interruptible','off', ...
%    'BackgroundColor',[0.45 0.45 0.45], ...
%    'ForegroundColor','white');
% % Text field
% textPos = [labelLeft+labelWidth labelBottom textWidth textHeight];
% callbackStr = 'greennoisedemo(''cutoff'')';
% hdl.Cutoff = uicontrol('Parent', FirDemoFig, ...
%    'BusyAction','Queue','Interruptible','off',...
%    'Style','edit', ...
%    'Units','normalized', ...
%    'Position',textPos, ...
%    'Horiz','right', ...
%    'Background','white', ...
%    'Foreground','black', ...
%    'String','0.5','Userdata',0.5, ...
%    'callback',callbackStr);
% 
% %===================================
% % Filter order label and text field
% labelBottom=top-2*textHeight-spacing;
% labelLeft = left;
% labelPos = [labelLeft labelBottom labelWidth textHeight]
% h = uicontrol('Parent', FirDemoFig, ...
%    'Style','text', ...
%    'Units','normalized', ...
%    'Position',labelPos, ...
%    'String','Order:', ...
%    'Horiz','left', ...
%    'Interruptible','off', ...
%    'Background',[0.45 0.45 0.45], ...
%    'Foreground','white');
% % Text field
% textPos = [labelLeft+labelWidth labelBottom textWidth textHeight];
% callbackStr = 'greennoisedemo(''order'')';
% hdl.Order = uicontrol('Parent', FirDemoFig, ...
%    'BusyAction','Queue','Interruptible','off',...
%    'Style','edit', ...
%    'Units','normalized', ...
%    'Position',textPos, ...
%    'Horiz','center', ...
%    'Background','white', ...
%    'Foreground','black', ...
%    'String','15','Userdata',15, ...
%    'Callback',callbackStr);
% 

% Generate Halftone Frame
framePos=[0.17+5*spacing spacing+(yRef-0.1) btnWid+2*spacing btnHt+2*spacing];
h=uicontrol( 'Parent', FirDemoFig, ...
   'Style','frame', ...
   'Units','normalized', ...
   'Position',framePos, ...
   'BackgroundColor',bgc);

%====================================
% The Generate Halftone button
labelStr='Generate Halftone';
callbackStr='greennoisedemo(''GenerateHalftone'');';
btnPos=[framePos(1)+spacing framePos(2)+spacing btnWid btnHt];
hdl.Apply=uicontrol('Parent', FirDemoFig, ...
   'BusyAction','Queue','Interruptible','off',...
   'Style','pushbutton', ...
   'Units','normalized', ...
   'Position',btnPos, ...
   'Enable','off', ...
   'String',labelStr, ...
   'Callback',callbackStr);

%%%%%%%%%Error filter Message%%%%%%%%%%%%%%%

 messagePos=[ framePos(1)-spacing framePos(2)-5*spacing textWidth*3 textHeight];
 h = uicontrol('Parent', FirDemoFig, ...
    'Style','text', ...
   'Units','normalized', ...
   'Position',messagePos, ...
   'Horiz','left', ...
   'String','The Error filter coefficients are Stucki coefficients', ...
   'Interruptible','off', ...
   'BackgroundColor',[0.8 0.8 0.8], ...
   'ForegroundColor','red');

%====================================
% The INFO button
labelStr='Info';
callbackStr='helpwin greennoisedemo';
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
setUpEdge(FirDemoFig);
GenerateHalftone(FirDemoFig);
set(FirDemoFig,'HandleVisibility','Callback');
set([hdl.Help hdl.Close] , 'enable', 'on');
return


%%%
%%%  Sub-Function - cutoff
%%%

function textHysteresis(DemoFig)

if nargin < 1
    DemoFig = gcbf;
end    
    
hdl = get(DemoFig,'Userdata');
v = get(hdl.Hysteresis,'Userdata');
s = get(hdl.Hysteresis,'String');
vv = eval(s,num2str(v));
if isempty(vv) | ~isreal(vv)
     vv = v; 
     set(hdl.Hysteresis, 'String', num2str(v))
    return
end

res = sprintf('%s', num2str(vv)');

set(hdl.Hysteresis,'Userdata',vv,'String',res);

setUpEdge;
return



%%%
%%%  Sub-Function - order
%%%

function order()


v = get(gcbo,'Userdata');
s = get(gcbo,'String');
vv = eval(s,num2str(v));
if isempty(vv)  | ~isreal(vv) | vv(1)<5, 
   vv = v; 
   set(gcbo, 'String', num2str(v))
   return
end
vv = round((vv(1)-1)/2)*2+1;
set(gcbo,'Userdata',vv,'String',num2str(vv))
DesignFilter
return


%%%
%%%  Sub-Function - RadioUPDATE
%%%

function RadioUPDATE(s)
DemoFig = gcbf;
hdl=get(DemoFig,'Userdata');
set([hdl.Scan1 hdl.Scan2], 'value', 0);
switch s %  Enable selected button
case 1
   set(hdl.Scan1,'value',1)
case 2
   set(hdl.Scan2,'value',1) 
end

set(hdl.Scan1,'Userdata',s) 
setUpScan(DemoFig);
return

function RadioUPDATE1(s)
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
setUpBtn(DemoFig);
return


%%%%%%%% For my application this needs to be called set halftoning parameters

function SetHalftoningParam(DemoFig)



return;

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

green_halftone = get(hdl.FilteredImage,'Cdata');
imwrite(green_halftone,'green_halftone.tif');

set(DemoFig,'Pointer','arrow');
set(hdl.SaveHalftone, 'Enable', 'off')
drawnow
setstatus(DemoFig, '');
return;



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

inp = inp*255; %Bring levels to -1 to 1 range

Choice = get(hdl.EdgeType,'value'); %Fixed or Adaptive

%%%%%%% Set Halftoning Parameters %%%%%%%%%

G = get(hdl.Slider,'value');
v=1;
NSt = [0 .6 0; 0.4 0 0];  % Standard Stucki Weights
scan = get(hdl.Scan1,'Userdata');
switch scan 
case 1    
   dir = 1;
case 2
   dir = -1;
end   

switch Choice
case 1 %fixed Green Noise halftoning
     filterMethod = get(hdl.GuessMenu,'value');
     switch filterMethod
     case 1
     fc = [1 5 3; 7 (-99*16) 0]/16; %FS
     case 2
     fc = get(hdl.Hysteresis,'Userdata'); %User Defined
     end
    
     OUT = green_fixed(inp,fc,NSt,G,dir,v);
case 2
     
     filterMethod = get(hdl.GuessMenu,'value');
     switch filterMethod
     case 1
        fc = [1 5 3; 7 (-99*16) 0]/16; %FS
     case 2
        fc = get(hdl.Hysteresis,'Userdata'); %User Defined
     end    
    
     [OUT,b] = green_adap(inp,fc,NSt,G,dir,v);
end
   
%%%% Assign Output for Halftone Retrieval %%%%%
RES = OUT;                                    % 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

set(DemoFig,'Pointer','arrow')

drawnow
setstatus(DemoFig, '');
set(hdl.Apply, 'Enable', 'on')

return

%%%
%%%  Sub-function - SliderUpdate
%%%

function SliderUpdate(DemoFig)

if nargin<1
   DemoFig = gcbf;
end

handles = get(DemoFig,'UserData');
hSlider=handles.Slider;
return


%%
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


function setUpEdge(DemoFig)

if nargin<1
   DemoFig = gcbf;
end

setstatus(DemoFig, '');
set(gcf,'Pointer','watch');
hdl = get(DemoFig,'Userdata');

check = get(hdl.GuessMenu,'value');

if (check == 1)
    set(hdl.Hysteresis,'Enable','off');
end

if (check == 2)
    set(hdl.Hysteresis,'Enable','on');
end


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

function setUpBtn(DemoFig)

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
%%%  Sub-Function - DesignFilter
%%%

% function DesignFilter(DemoFig)
% 
% if nargin<1
%    DemoFig = gcbf;
% end
% 
% setstatus(DemoFig, 'Designing Filter...');
% set(gcf,'Pointer','watch');
% hdl=get(DemoFig,'Userdata');
% 
% edgecolor = get(gca,'colororder'); edgecolor = edgecolor(1,:);
% 
% order = get(hdl.Order,'UserData'); % Filter order
% cutoff = get(hdl.Cutoff,'UserData'); % Filter cutoff frequency
% 
% % Create desired frequency response
% [f1,f2] = freqspace(order,'meshgrid');
% d = find(f1.^2+f2.^2 < cutoff^2);
% Hd = zeros(order);
% Hd(d) = 1;
% if get(hdl.EdgeType,'value')==2, Hd = 1-Hd; end % If highpass
% 
% method = get(hdl.Btn1,'Userdata');
% 
% % Get window if necessary
% if method==2 | method==3, % Get window
%    wind = get(hdl.Window,'value');
%    windows = get(hdl.Window,'string');
%    w = feval(lower(deblank(windows(wind,:))), order);
% end
% 
% % Design filter
% switch method
% case 1,
%    h = fsamp2(Hd);
% case 2,
%    h = fwind1(Hd,w);
% case 3,
%    h = fwind2(Hd,w*w');
% case 4,
%    F = [0 cutoff-0.05 cutoff+0.05 1];
%    M = [1 1 0 0];
%    if get(hdl.EdgeType,'value')==2, M = 1-M; end
%    h = ftrans2(remez(order-1,F,M));
% end
% 
% hmin = min(h(:)); hmax = max(h(:));
% facecolor=get(hdl.Filter, 'color');
% 
% % Get rid of old surface if one exists
% delete(findobj(get(hdl.Filter,'Children'),'type','surface'))
% 
% hndl = surface(h,'Parent',hdl.Filter,'FaceColor',facecolor,...
%    'EdgeColor',edgecolor,'FaceLighting','none','EdgeLighting', 'flat');
% set(hdl.Filter, 'view',[-37.5, 30]); 
% set(hdl.Filter,'xlim',[1 order],'ylim',[1 order])
% set(hdl.Filter,'zlim',[hmin-(.05*abs(hmin)) hmax*1.05])
% set(hdl.Filter, 'XGrid', 'on', 'YGrid', 'on', 'ZGrid', 'on');
% % set(get(hdl.Filter,'title'),'string','Filter coefficients')
% 
% % Compute frequency response
% facecolor=get(hdl.Response, 'color');
% [r,f1,f2] = freqz2(h,32,32);
% rmin = min(r(:)); rmax = max(r(:));
% 
% % Get rid of old surface if one exists
% delete(findobj(get(hdl.Response,'Children'),'type','surface'))
% 
% hndl = surface(f1,f2,r,'Parent',hdl.Response,'FaceColor',facecolor,...
%    'EdgeColor',edgecolor,'FaceLighting','none','EdgeLighting', 'flat');
% % set(get(hdl.Response,'title'),'string','Frequency Response')
% set(hdl.Response, 'view',[-37.5, 30]); 
% set(hdl.Response,'zlim',[rmin-(.05*abs(rmin)) rmax*1.05])
% set(hdl.Response, 'XGrid', 'on', 'YGrid', 'on', 'ZGrid', 'on');
% set(DemoFig,'Pointer','arrow')
% % rotate3d on;
% 
% drawnow
% setstatus(DemoFig, 'Press ''Generate Halftone'' to generate the halftone with the selected options');
% set(hdl.Apply, 'Enable', 'on')
% return

   
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
% h may be the user defined filter
%J = mat2gray(filter2(h,I));
J = 255*CallFilter(DemoFig);

% Calculate the 4 Figures of Merit right here 
measure_psnr = psnr(ORIG,J);
measure_wsnr = wsnr(ORIG,J);
measure_ldm = ldm(ORIG,J);
measure_uqi = img_qi(ORIG,J);

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
   lena = imread('lena.tif');
   img = double(lena);
case 'Peppers'
   peppers = imread('peppers.tif');
   img = double(peppers);
case 'Barbara',
   barbara = imread('barbara.tif');
   img = double(barbara);
case 'Boat',
   boat = imread('boat.tif');
   img = double(boat);
case 'Trees'
   trees = imread('trees.tif');
   img = double(trees);
case 'Clock'
   clock = imread('clock.tif');
   img = double(clock);    
otherwise 
   error('FIRDEMO: Unknown Image Option!');
end

img = double(img)/255;
set(hdl.OriginalImage, 'Cdata', img);


%% I would need a function to set all DemoFig halftoning parameters
set(get(hdl.OrigImageAxes,'title'),'string',['Original Image']);
drawnow
set(DemoFig,'Pointer','arrow')
if callb
   GenerateHalftone(DemoFig); 
end
drawnow
return;

