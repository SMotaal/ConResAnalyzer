classdef PatchGeneratorPanel < Grasppe.Occam.Process
  %PATCHGENERATORPARAMETERSPANEL Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    jParametersPanel, hParametersPanel
    jApplyButton, hApplyButton
    jPatchParametersPanel, hPatchParametersPanel
    jScreenParametersPanel, hScreenParametersPanel
    jPrintParametersPanel, hPrintParametersPanel
    jScanParametersPanel, hScanParametersPanel
    jImagePanel, hImagePanel
    hScrollPane;
    hFrame;
    
  end
  
  events
    % ParametersChanged
    ParametersApplied
  end
  
  properties
    PatchGeneratorParameters = Grasppe.ConRes.PatchGenerator.Models.PatchGeneratorParameters;
  end
  
  methods
    function obj = PatchGeneratorPanel()
      obj=obj@Grasppe.Occam.Process();
      obj.permanent = true;
    end
    
    function output = Run(obj)
      output = [];
      obj.initializePanel;
      
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
      try obj.createPanel(); end
      try obj.applyChanges; end
    end
    
    function createPanel(obj)
      % mjLink
      
      disp('Creating Panel');
      
      cFigure = get(0,'CurrentFigure');
      
      oFrame  = {'Position', get(0,'Screensize'), 'ToolBar','none', 'color', [1 1 1] * 0.45, 'Renderer', 'OpenGL'};
      
      if ~isempty(cFigure) && ishandle(cFigure) && isvalid(cFigure)
        hFrame = figure(cFigure, oFrame{:});
      else
        hFrame = figure(oFrame{:});
      end
      
      set(hFrame, 'CloseRequestFcn', @(src, event) delete(obj) );
      
      obj.hFrame      = hFrame;     
      
      jFrame = get(handle(hFrame),'JavaFrame');
      
      jPane = jFrame.fHG1Client.getContentPane;      
      
      obj.jParametersPanel = com.grasppe.conreslabs.panels.PatchGeneratorParametersPanel;
      jPane.add(obj.jParametersPanel, java.awt.BorderLayout.EAST);
      
      obj.jImagePanel = com.grasppe.jive.components.ImagePanel;
      jPane.add(obj.jImagePanel, java.awt.BorderLayout.CENTER);
      
      obj.jApplyButton = handle(obj.jParametersPanel.getApplyButton(),'CallbackProperties');
      
      set(obj.jApplyButton, 'actionPerformedCallback', @(src, event)obj.applyChanges);
      
      
      drawnow expose update;
      
      jFrame.fHG1Client.toFront();
%       
%       obj.jParametersPanel.grabFocus();

      
      try jFrame.setMaximized(true); end
      
      tic; 
        frame = handle(frame);
%         set(frame, 'Visible', 'off')
%         set(frame, 'Visible', 'on');
        obj.jParametersPanel.grabFocus();
        obj.jParametersPanel.transferFocus();
      toc;
      
      drawnow expose update;
      
      % obj.jParametersPanel.transferFocus();      
    end
    
    function applyChanges(obj)
      import Grasppe.ConRes.PatchGenerator.*;
      
      disp('Applying Changes');
      
      parameters = obj.jParametersPanel.getValues();
      
      try parameters = hashmap2struct(parameters); end %, true); end
      
      
      try
        Patch     = parameters.Patch; % Parameters.PatchParameters;
        Screen    = parameters.Screening; %Parameters.ScreenParameters;
        Print     = parameters.Printing; % Parameters.PrintParameters;
        Scan      = parameters.Scanning; % Parameters.ScanParameters;
        
        for fn = fieldnames(parameters)'
          % fn iterates through the field names of S
          % fn is a 1x1 cell array
          fn = char(fn);
          fnl = lower(fn);
          
          if strfind(fnl, 'fourier')==1;
            disp(fn);
            Processors.(fnl) = parameters.(fn);
          elseif strfind(fnl, 'function')==1;
            disp(fn);
            Processors.(fnl) = parameters.(fn);
          else
            % beep;
          end
        end
        
        
        obj.PatchGeneratorParameters.Patch  = Patch;
        obj.PatchGeneratorParameters.Screen = Screen;
        obj.PatchGeneratorParameters.Print  = Print;
        obj.PatchGeneratorParameters.Scan   = Scan;
        obj.PatchGeneratorParameters.Processors = Processors;
      end
      
      % beep;
      notify(obj, 'ParametersApplied');
    end
    
    function setImage(obj, img)
      disp('Setting Image');
      if isempty(img)
        obj.jImagePanel.setPreviewImage(null);
      else
        obj.jImagePanel.setPreviewImage(im2java2d(img));
      end
        % clf;
        % hIm = imshow(image); % ,'InitialMagnification', 100, 'Border', 'loose');
        % hSP = imscrollpanel(gcf,hIm);
        % obj.hFrame      = hFrame;
%         obj.jParametersPanel.getWidth
%         
%         hScrollpane = obj.hScrollPane;
% 
%         hAxes  = axes('Visible', 'off', 'Parent', obj.hFrame);
%         hImage = image(img,'Visible', 'off', 'Parent', hAxes); %imshow(img, 'Parent', hAxes);
%         
%         delete(hAxes);
%                 
%         if ~(~isempty(hScrollpane) && ishandle(hScrollpane)) % && isvalid(hScrollpane))
%           hScrollpane = imscrollpanel(obj.hFrame, hImage);
%           obj.hScrollPane = hScrollpane;
%           api = iptgetapi(hScrollpane);
%         else
%           api = iptgetapi(hScrollpane);
%           api.replaceImage(hImage);
%         end
%                 
%         
        
%         if ~isempty(hImage)
%           
%         end
        
%         api.setMagnification(1.0);
%         
%         set(hScrollpane, 'Unit', ' pixels', 'Position', get(0,'Screensize') - [0 0 obj.jParametersPanel.getWidth 0]);
%         
        % set(hScrollpane, 'ResizeFcn', []);
        


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

