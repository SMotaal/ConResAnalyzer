classdef PatchGeneratorPanel < Grasppe.ConRes.PatchGenerator.Processors.Process
  %PATCHGENERATORPARAMETERSPANEL Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    jParametersPanel, hParametersPanel
    jApplyButton, hApplyButton
    jPatchParametersPanel, hPatchParametersPanel
    jScreenParametersPanel, hScreenParametersPanel
    jPrintParametersPanel, hPrintParametersPanel
    jScanParametersPanel, hScanParametersPanel
    
  end
  
  events
    ParametersChanged
    ParametersApplied
  end
  
  properties
    PatchGeneratorParameters = Grasppe.ConRes.PatchGenerator.Parameters.PatchGeneratorParameters;
  end
  
  methods
    function obj = PatchGeneratorPanel()
      
    end
    
    function Run(obj)
      obj.initializePanel;
      
%       %# figure
%       hFig = gcf;
% 
%       %# handle figure resize events
%       hAx = gca;
%       set(hFig, 'ResizeFcn',{@obj.onResize,hAx})
% 
%       %# call it at least once
%       feval(@obj.onResize,hFig,[],hAx);

%       keyboard;
%     jFrame = get(handle(gcf),'JavaFrame')
%     jFrame.setMaximized(true);   % to maximize the figure % false: to un-maximize the figure
    
      obj.applyChanges;
    end
%     
%     function onResize(obj, source, event,hAx)
%       %# get axes limits in pixels
%       oldUnits = get(hAx, 'Units');    %# backup normalized units
%       set(hAx, 'Units','pixels')
%       pos = get(hAx, 'Position');
%       set(hAx, 'Units',oldUnits)       %# restore units (so it auto-resize)
%       
%       %# display the top left part of the image at magnification 100%
%       xlim(hAx, [0 pos(3)]+0.5)
%       ylim(hAx, [0 pos(4)]+0.5)
%     end
    
    
    function parameters = getParameters(obj)
      parameters = obj.PatchGeneratorParameters;
    end
    
    function initializePanel(obj)
      % try
        obj.createPanel();
%       catch err
%         mjLink;
%         rethrow err;
%         try
%           obj.createPanel();
%         catch err2
%           rethrow(err2)
%         end
%       end
    end
    
    function createPanel(obj)
      % mjLink
      
      set(gcf, 'CloseRequestFcn', @(src, event) delete(obj) );
      
      [obj.jParametersPanel obj.hParametersPanel] = javacomponent('com.grasppe.conreslabs.panels.PatchGeneratorParametersPanel', 'East');
      
      obj.jPatchParametersPanel   = handle(obj.jParametersPanel.getPatchParametersPanel);
      obj.jScreenParametersPanel  = handle(obj.jParametersPanel.getScreeningParametersPanel);
      obj.jPrintParametersPanel   = handle(obj.jParametersPanel.getPrintingParametersPanel);
      obj.jScanParametersPanel    = handle(obj.jParametersPanel.getScanningParametersPanel);
      
      obj.jApplyButton = handle(obj.jParametersPanel.getApplyButton(),'CallbackProperties');
      
      set(obj.jApplyButton, 'actionPerformedCallback', @(src, event)obj.applyChanges);
      
    end
    
    function applyChanges(obj)
      import Grasppe.ConRes.PatchGenerator.*;
      
      % Patch Parameters
      Patch                   = Parameters.PatchParameters;
      Patch.MeanTone          = obj.jPatchParametersPanel.getMeanToneValue;
      Patch.Contrast          = obj.jPatchParametersPanel.getContrastValue;
      Patch.Resolution        = obj.jPatchParametersPanel.getResolutionValue;
      Patch.Size              = obj.jPatchParametersPanel.getPatchSizeValue;
            
      % Screen Parameters
      Screen                  = Parameters.ScreenParameters;
      Screen.Addressability   = obj.jScreenParametersPanel.getAddressabilityValue;
      Screen.Resolution       = obj.jScreenParametersPanel.getResolutionValue;
      Screen.Angle            = obj.jScreenParametersPanel.getAngleValue;
            
      % Print Parameters
      Print                   = Parameters.PrintParameters;
      Print.DotGain           = obj.jPrintParametersPanel.getDotgainValue;
      Print.Noise             = obj.jPrintParametersPanel.getNoiseValue;
      Print.Spread            = obj.jPrintParametersPanel.getSpreadValue;
      Print.Unsharp           = obj.jPrintParametersPanel.getUnsharpValue;
            
      % Scan Parameters
      Scan    = Parameters.ScanParameters;
      Scan.Resolution         = obj.jScanParametersPanel.getResolutionValue;
      Scan.Scale              = obj.jScanParametersPanel.getScaleValue;
            
      % Generator Parameters
      obj.PatchGeneratorParameters.Patch  = Patch;
      obj.PatchGeneratorParameters.Screen = Screen;
      obj.PatchGeneratorParameters.Print  = Print;
      obj.PatchGeneratorParameters.Scan   = Scan;

      % beep;
      notify(obj, 'ParametersApplied');
    end
    
    function delete(obj)
      % try delete(obj.jApplyButton); end %hApplyButton
      
      jComponents = {obj.jPatchParametersPanel, obj.jScreenParametersPanel, obj.jPrintParametersPanel, obj.jScanParametersPanel, obj.jParametersPanel};
      
      for j = 1:numel(jComponents)
        jObject = jComponents{j};
        try jObject = 0; end
        try delete(jObject); end
      end
      
      try delete(gcf); end
      
%       try delete(obj.jPatchParametersPanel);  end % hPatchParametersPanel
%       try delete(obj.jScreenParametersPanel); end % hScreenParametersPanel
%       try delete(obj.jPrintParametersPanel);  end % hPrintParametersPanel
%       try delete(obj.jScanParametersPanel);   end % hScanParametersPanel      
%       try delete(obj.jParametersPanel);       end %hParametersPanel
    end 

  end
  
end

