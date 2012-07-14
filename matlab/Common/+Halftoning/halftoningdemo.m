
% HALFTONINGDEMO 
% The halftoningdemo demonstrates a variety of halftoning
% methods and evaluates figures of merit for the
% halftones generated.  Halftoning methods are 
% categorized into grayscale and color.
%                                           
% To start the demo type "halftoningdemo" '
% 
% For more information on the halftoning demo
% click on "Info"
% For details on methods available in the grayscale and color
% categories click on their respective "Info" buttons
%
% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.
%  
% This program is free software; you can redistribute it and/or modify
% it under the terms of the GNU General Public License as published by
% the Free Software Foundation; either version 2 of the License, or
% (at your option) any later version.
%  
% This program is distributed in the hope that it will be useful,
% but WITHOUT ANY WARRANTY; without even the implied warranty of
% MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
% GNU General Public License for more details.
%  
% The GNU Public License is available in the file LICENSE, or you
% can write to the Free Software Foundation, Inc., 59 Temple Place -
% Suite 330, Boston, MA 02111-1307, USA, or you can find it on the
% World Wide Web at http://www.fsf.org.
%  
% Programmers:	Vishal Monga  
% Version:      @(#)halftoningdemo.m	1.0 01/14/02
% 
% The authors are with the Department of Electrical and Computer
% Engineering, The University of Texas at Austin, Austin, TX.
% They can be reached at vishal@ece.utexas.edu.
% They are also with the Embedded Signal Processing
% Laboratory in the Dept. of ECE., http://signal.ece.utexas.edu.
%
% The appearance of this demonstration is modeled after the
% appearance of the DMT TEQ toolbox demo written by Ming Ding
% and Zukang Shen of the Embedded Signal Processing Laboratory
% at The University of Texas at Austin.

% Authored July 2002 by Vishal Monga

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.

function  halftoningdemo(action);

if nargin < 1,
   action='init';
end;

if strcmp(action,'init'),
h0 = figure('Position',[232 50 575 600], ...
   'ToolBar','none',...
   'name','Forward Halftoning Toolbox 1.2',...
   'NumberTitle','off',...
   'Menubar','figure');

figureColor = get(h0, 'Color');    

%Welcome textbox
h1 = uicontrol('Units','normalized', ...
   'Style', 'text', ...
   'HorizontalAlignment','center', ...
   'Units','normalized', ...
   'BackgroundColor', [1 1 1], ...
   'Min', 0, ...
   'Max', 2, ...
   'Value', [], ...
   'Enable', 'inactive', ...
   'Position',[0.05 0.76 0.36 0.17], ...
   'Callback', '', ...
   'String', {'Thank you for using', 'Halftoning Toolbox 1.2 ',...
        'please choose from a category of ', 'Halftoning Methods'});

%Ordered/Dither Screening
GrayHdl = uicontrol( ...
    'Units','normalized',...
   'Style', 'pushbutton', ...
   'Position', [0.05 0.57 0.36 0.06], ...
   'String', 'Grayscale Halftoning Methods', ...
   'Callback', 'grayscaledemo'); 

%Classical Error Diffusion halftoning
ColorHdl = uicontrol( ...
    'Units','normalized',...
   'Style', 'pushbutton', ...
   'Position', [0.05 0.46 0.36 0.06], ...
   'String', 'Color Halftoning Methods', ...
   'Callback', 'colordemo'); 

%Display the ESPL Logo
ax2=axes( 'Position', [0.05 0.03 0.36 0.40],'visible', 'off');
LogoHdl=imread('ESPLogo','gif');
imshow(LogoHdl);

% create a listbox for displaying the 'about' information
aboutListPos=[0.45,0.25,0.5,0.69];

aboutinfo = {...
'The halftoningdemo demonstrates a variety of'
'halftoning methods and evaluates figures of merit'
'for the halftones generated.  This demo is part'
'of a halftoning toolbox written by Vishal Monga,'
'Niranjan Damera-Venkata, Hamood Rehman,'
'and Prof. Brian L. Evans at '
'The University of Texas at Austin, Austin, TX, USA.'
'Halftoning research by the research group of Prof.'
'Brian L. Evans at UT Austin is described on the Web at'
' '
'http://www.ece.utexas.edu/~bevans/projects/halftoning/'
' '
'The halftoning methods in this demo are categorized into'
'color and grayscale methods. (buttons on the left)'
'Click on either of the two buttons to proceed '
'                                               '
'This halftoning toolbox is copyright (c) 1999-2005 by'
'The University of Texas.  All Rights Reserved.'
' '
'This program is free software; you can redistribute it'
'and/or modify it under the terms of the GNU General '
'Public License as published by the Free Software '
'Foundation; either version 2 of the License, or '
'(at your option) any later version.'
' '
'This program is distributed in the hope that it will be'
'useful, but WITHOUT ANY WARRANTY; without even the implied'
'warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR'
'PURPOSE.  See the GNU General Public License for more '
'details.'
' '
'The GNU Public License is available in the file LICENSE,'
'or you can write to the Free Software Foundation, Inc.,'
'59 Temple Place - Suite 330, Boston, MA 02111-1307, USA,'
'or you can find it on the Web at http://www.fsf.org.'};


aboutListH = uicontrol( ...
   'Style', 'list', ...
   'HorizontalAlignment','left', ...
   'Units','normalized', ...
   'BackgroundColor', figureColor, ...
   'Min', 0, ...
   'Max', 2, ...
   'Value', [], ...
   'Enable', 'inactive', ...
   'Position', aboutListPos, ...
   'Callback', '', ...
   'String', aboutinfo, ...
   'Tag', 'AboutListbox');

   % help button
   labelStr='Info';
   callbackStr='helpwin halftoningdemo';
   helpHndl=uicontrol( ...
      'Style','pushbutton', ...
      'Units','normalized', ...
      'Position',[0.45 0.13 0.22 0.07], ...
      'String',labelStr, ...
      'Callback',callbackStr);
  
  %close button
  backH = uicontrol( ...
   'Style', 'pushbutton', ...
   'Units', 'normalized', ...
   'Position', [0.73 0.13 0.22 0.07], ...
   'String', 'Close', ...
   'Tag', 'return', ...
   'Callback', 'close(gcbf)'); 

elseif strcmp(action,'info'),
   ttlStr = get(gcf,'name');
   myFig = gcf;
   
   topic1 =  ['HALFTONING DEMO'];
   helptop1 = [...
           
       ' The halftoningdemo demonstrates a variety of halftoning'
       ' methods and evaluates figures of merit for the         '
       ' halftones generated.  Halftoning methods include       '
       '                                                        '
       ' 1. classical and user-defined screens                  '
       ' 2. classical error diffusion methods                   ' 
       ' 3. edge enhancement error diffusion                    ' 
       ' 4. green noise error diffusion                         '
       ' 5. block error diffusion                               '
       '                                                        '
       ' Figures of merit measures include                      '
       '                                                        '
       ' 1. peak signal-to-noise ratio (PSNR)                   '
       ' 2. weighted signal-to-noise ratio (WSNR)               '
       ' 3. linear distortion measure (LDM)                     '
       ' 4. universal quality index (UQI)                       '
       '                                                        '
       ' To start the demo type "halftoningdemo"                '
            
   ];
   
   
   str =  { topic1 helptop1};
   
   helpwin(str,'Topic 1','HALFTONING TOOLBOX        ');                   
   return  

end;


