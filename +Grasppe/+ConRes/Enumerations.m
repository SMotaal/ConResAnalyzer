classdef Enumerations < Grasppe.Core.ValueEnumeration
  %ENUM Summary of this function goes here
  %   Detailed explanation goes here
  
%   properties
%     Value;
%   end
%   
%   methods (Access = private)
%     function obj=CONRES(value)
%       persistent lastValue;
%       if nargin==0, value = lastValue; end
%       obj.Value = value;
%       lastValue = value;
%     end
%   end
%   
%   methods
%     function b = ctranspose(a)
%       b = a.Value;
%       try
%         b  = char(regexp(b, '(?=.)\w*$', 'match'));
%       end
%     end
%     
%     function b = transpose(a)
%       b = a.Value;
%     end
%     
%     
%     function c = eq(a,b)
%       c = isequal(a.Value, b.Value);
%     end
%     
%     function c = ne(a,b)
%       c = ~isequal(a.Value, b.Value);
%     end
%     
%   end
%   
  enumeration
    
    %% Patch
    PatchTone('Patch.Mean'), ...
      Mean, RTV, RefTone, ReferenceToneValue
    PatchFrequency('Patch.Resolution'), ...
      Resolution, LineFrequency, PatchResolution
    PatchContrast('Patch.Contrast'),  ...
      Contrast
    PatchSize('Patch.Size'), ...
      Size
    
    %% Screen
    ScreenAddressability('Screen.Addressability'), ...
      Addressability, SPI
    ScreenResolution('Screen.Resolution'), ...
      Ruling, LPI, LineRuling
    ScreenAngle('Screen.Angle'), ...
      Angle, Theta,
    ImageResolution('Screen.PixelResolution'), ...
      PixelResolution, PPI
    
    
    %% Print
    ToneValueIncrease('Print.Gain'), ...
      Gain, TVI, DotGain
    Noise('Print.Noise')
    Radius('Print.Spread'), ...
      Spread
    Blur('Print.Unsharp'), ... 
      Unsharp
    
    %% Scan
    ScanResolution('Scan.Resolution'), ...
      DPI
    ScanScale('Scan.Scale'), ...
      Scale
    
  end
  
end

