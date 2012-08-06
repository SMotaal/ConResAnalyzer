classdef PatchGeneratorPanel < Grasppe.Occam.Process
  %PATCHGENERATORPARAMETERSPANEL Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    jParametersPanel, hParametersPanel
    jApplyButton, hApplyButton
    jFrame, hFrame
    jPane
    
    jReferences = {'jParametersPanel', 'hParametersPanel', ...
      'jApplyButton', 'hApplyButton', ...
      'jFrame', 'hFrame', ...
      'jPane'...
      }
    
    hAxes={}
    
    mAxes;
    
    jComponents={}
    
    panelCode = { ...
      'Patch={Resolution=0.625, Contrast=5, Mean=50, Size=5.3},'
      'Screening={Angle=37.5, Resolution=175.0, Addressability=2450.0},'
      'Printing={Blur=100.0, Radius=1.0, Gain=0.0, Noise=0},'
      'Scanning={Resolution=1200, Scale=100},'
      'Function-0-PatchFFT={ID=Function-0-PatchFFT, Expression=I:PFFT;},'
      'Function-1-ScreenImage={ID=Function-1-ScreenImage, Expression=I:SIMG;},'
      'Function-2-ScreenFilterI={ID=Function-2-ScreenFilterI, Expression=I:iFFTL(PFFT.*norverse(logabs(SFFT)));},'
      'Function-3-ScreenPassFilter={ID=Function-3-ScreenPassFilter, Expression=I:iFFTL(1-binor(logabs(SFFT)/0.779));},'
      'Function-4-ScreenFilterII={ID=Function-4-ScreenFilterII, Expression=I:iFFTL(PFFT.*binorverse(logabs(SFFT)/0.24));},'
      'Function-5-IdealImage={ID=Function-5-IdealImage, Expression=I:CIMG;},'
      'Function-6-IdealFilterII={ID=Function-6-IdealFilterII, Expression=I:iFFTL(PFFT.*binor(logabs(CFFT)/0.4));}'      };
  end
  
  events
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
    
    
    function parameters = getParameters(obj)
      parameters = obj.PatchGeneratorParameters;
    end
    
    function initializePanel(obj)
      try obj.createPanel(); end
      try obj.applyChanges; end
    end
    
    function createPanel(obj)
      
      disp('Creating Panel');
      
      cFrame = get(0,'CurrentFigure');
      
      oFrame  = {'Position', get(0,'Screensize'), 'ToolBar','none', ...
        'color', [1 1 1] * 0.45, 'Renderer', 'OpenGL', ...
        'CloseRequestFcn', @(src, event) delete(obj) ...
        };
      
      if ~isempty(cFrame) && ishandle(cFrame) && isvalid(cFrame)
        hFrame = figure(cFrame, oFrame{:});
      else
        hFrame = figure(oFrame{:});
      end
      
      jFrame = get(handle(hFrame),'JavaFrame');
      
      obj.jFrame  = jFrame;
      obj.hFrame  = hFrame;
      
      jPane = jFrame.fHG1Client.getContentPane;
      
      obj.jPane = jPane;
      
      [jPanel hPanel] = javacomponent('com.grasppe.conreslabs.panels.PatchGeneratorParametersPanel','East');
      
      obj.jParametersPanel    = jPanel;
      obj.hParametersPanel    = handle(jPanel,'CallbackProperties');
      
      panelCode = strcat(obj.panelCode{:});
      
      obj.jParametersPanel.setValues(panelCode);  %'Patch={Resolution=0.625, Contrast=50, Mean=30, Size=5.3}, Screening={Angle=37.5, Resolution=175.0, Addressability=2450}, Printing={Blur=100.0, Radius=5.0, Gain=0.0, Noise=0.0}, Scanning={Resolution=1200.0, Scale=100.0}, Function-0-PatchFFT={ID=Function-0-PatchFFT, Expression=patchFFT;}, Function-1-ScreenImage={ID=Function-1-ScreenImage, Expression=screenImage;}, Function-2-ScreenPassFilter={ID=Function-2-ScreenPassFilter, Expression=imadjust(inverseFFT(1-binarize(normalize(log(abs(screenFFT)))/0.779)));}, Function-3-ScreenFilterI={ID=Function-3-ScreenFilterI, Expression=imadjust(inverseFFT(multiply(patchFFT/1-normalize(log(abs(screenFFT))))));}, Function-4-ScreenFilterII={ID=Function-4-ScreenFilterII, Expression=imadjust(inverseFFT(multiply(patchFFT/binarize(1-normalize(log(abs(screenFFT)))/0.24))));}, Function-5-IdealImage={ID=Function-5-IdealImage, Expression=idealImage;}, Function-6-IdealFilterII={ID=Function-6-IdealFilterII, Expression=imadjust(inverseFFT(multiply(patchFFT/binarize(normalize(log(abs(idealFFT)))/0.4))));}');
      
      obj.jComponents{end+1}  = jPanel;
      
      obj.hParametersPanel.PropertyChangeCallback = @(src, e)obj.eventCallback(src, e, 'updatePanel');
      
      obj.jApplyButton = obj.jParametersPanel.getApplyButton();
      obj.hApplyButton = handle(obj.jApplyButton,'CallbackProperties');
      
      obj.hApplyButton.ActionPerformedCallback = @(src, e)obj.eventCallback(src, e, 'applyChanges');
      
      drawnow expose update;
      
      try obj.jParametersPanel.enableFullScreenMode(jFrame.fHG1Client.getWindow); end
      
      try jFrame.fHG1Client.toFront(); end
      try jFrame.setMaximized(true); end
      
      % frame = handle(frame);
      obj.jParametersPanel.grabFocus();
      obj.jParametersPanel.transferFocus();
      
      drawnow expose update;
      
    end
    
    function eventCallback(obj, source, event, id)
      
      e     = get(handle(event));
      e.id  = id;
      
      try e.SourceName  = char(source.getName);   end
      try e.ToolTipText = source.ToolTipText;     end
      
      %structdisp(e);
      
      switch id
        case 'applyChanges'
          obj.applyChanges;
        case 'updateImage'
        case 'updatePanel'
          try
            if isequal(e.PropertyName, 'Panel.Layout')
              drawnow expose update;
              obj.jParametersPanel.repaint;
              try obj.jFrame.fHG1Client.toFront(); end
              %obj.jParametersPanel.grabFocus();
              %obj.jParametersPanel.transferFocus();
              %               obj.jParametersPanel.grabFocus();
              %               obj.jParametersPanel.transferFocus();
            end
          catch err
            disp(err);
            beep;
          end
      end
      return;
    end
    
    function applyChanges(obj)
      import Grasppe.ConRes.PatchGenerator.*;
      
      disp('Applying Changes');
      
      parameters = obj.jParametersPanel.getValues();
      
      try parameters = hashmap2struct(parameters); end
      
      try
        Patch         = parameters.Patch;
        Screen        = parameters.Screening;
        Print         = parameters.Printing;
        Scan          = parameters.Scanning;
        
        Processors    = struct;
        
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
          elseif strfind(fnl, 'display')==1;
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
      catch err
        disp(err);
      end
      
      % beep;
      notify(obj, 'ParametersApplied');
    end
    
    function setImage(obj, img)
      % disp('Setting Image');
      hFrame      = obj.hFrame;
      
      set(hFrame, 'ResizeFcn', @(src, e) obj.resizeCallback(src, e));
      
      hAxis  = obj.newAxes('xtick',[],'ytick',[], 'Visible', 'off');
          
      imgd = im2double(img);
      if ~isreal(imgd)
        disp('wait a second');
      end      
      
      hold on;
      hImage = imshow(imgd, 'Parent', hAxis, 'InitialMagnification','fit'); %'Border','loose');
      
      set(hAxis,'xtick',[],'ytick',[], 'LooseInset', [0,0,0,0], 'Clipping','on', 'Box', 'on', 'color', [1 1 1] * 0.15, 'Visible', 'off'); %, 'DataAspectRatio', [1 1 1]);
      
      obj.layoutAxes;
      
    end
    
    function resizeImageAxis(obj, hAxis)
      try
        aP    = pixelPosition(hAxis);
        aW    = aP(3);
        aH    = aP(4);
        
        hImg  = findobj('Parent', hAxis, 'Type', 'image');%get(hAxis, 'Children');
        
        iW    = max(get(hImg, 'XData'));
        iH    = max(get(hImg, 'YData'));
        
        % Centering Around
        aF    = [0 1];
        aX    = aW*aF - (aW-iW)/2;
        aY    = aH*aF - (aH-iH)/2;
        
        set(hAxis,'XLim', aX + 0.5, 'YLim', aY + 0.5);
      catch err
        disp(err);
      end
    end
    
    function clearAxes(obj)
      for hAxis = obj.hAxes
        try delete(hAxis); end
      end
      obj.hAxes = {};
      clf(obj.hFrame);
    end
    
    function hAxis = newAxes(obj, varargin)
      hFrame  = obj.hFrame;
      hAxis   = axes('Visible', 'off', 'Parent', hFrame, varargin{:}, 'box', 'on');
      
      obj.hAxes{end+1} = hAxis;
    end
    
    function resizeCallback(obj, source, event)
      obj.layoutAxes();
    end
    
    function resizeAxis(obj, h, m)
      hFrame    = obj.hFrame;
      childAxes = findobj('Parent', hFrame, 'Type', 'axes'); %findobj(hFrame, 'Children', 'Type', 'axes');
      
      nA      = numel(obj.hAxes);
      nC      = ceil(nA ^ 0.5); ...
        nC = nC + mod(nC,2);
      nR      = ceil(nA/nC); %max(1, ceil(nA/nC)-1);
      
      fP      = pixelPosition(hFrame);
      pX      = obj.jParametersPanel.getX;
      
      nW      = ceil((pX-1)/nC);
      nH      = ceil(fP(4)/nR);
      
      try
        
        if ~exist('m','var') || ~isnumeric(m)
          if isnumeric(h) && ishandle(h)
            m = find(childAxes==h);
          else
            m = find(cellfun(@(x) isequal(x,h), obj.hAxes),1,'first');
          end
        end
        
        mC  = mod(m-1,nC)+1;
        mR  = ceil(m/nC);
        mH  = 1;
        mW  = 1; ...
          if m==nA, mW = 1+nC-mC; end
        
        %[left bottom width height]
        mP  = [ (mC-1)*nW,    (mR-1)*nH,    mW*nW,    mH*nH ];
        
        try
          %disp([m nA mC mR mP]);
          if isnumeric(h) && ishandle(h)
            mP(2) = fP(4)-mP(2)-mP(4);
            set(h, 'Units', 'pixels', 'Position', mP, 'Visible', 'on');
          else
            b = h.getBounds;
            mP = int16([mP(1),fP(4)-mP(2),mP(3),mP(4)]);
            oP = int16([b.x, b.y, b.width, b.height]);
            if any(mP~=oP)
              h.setBounds(mP(1), mP(2), mP(3), mP(4));
            end
          end
        catch err
          disp(err)
        end
        
        % hFrame  = obj.hFrame;
        
        hAxes   = obj.hAxes;
        nA      = numel(hAxes);
        
        pFrame  = pixelPosition(hFrame);
        axesMap = zeros(pFrame([3 4])+1);
        
        for m = 1:nA
          hAxis = hAxes{m};
          pAxis = round(pixelPosition(hAxis))+1;
          try
            axesMap(pAxis(1):pAxis(1)+pAxis(3), pAxis(2):pAxis(2)+pAxis(4)) = m;
          catch err
            disp(err);
          end
        end
        
        obj.mAxes = axesMap;
        
        
      catch err
        disp(err)
      end
    end
    
    function layoutAxes(obj)
      obj.validateAxes();
      
      hFrame  = obj.hFrame;
      
      hAxes   = obj.hAxes;
      nA      = numel(hAxes);
      
      for m = 1:nA
        hAxis   = hAxes{m};
        if isnumeric(hAxis) && ishandle(hAxis)
          hImg = []; hChd = [];
          try hImg = findobj('Parent', hAxis, 'Type', 'image'); end
          try hChd = findobj('Parent', hAxis); end
          
          obj.resizeAxis(hAxis, m);
          
          if numel(hImg)==1 && ishandle(hImg)
            obj.resizeImageAxis(hAxis);
          end
        end
      end
      
%       hFrame  = obj.hFrame;
%       
%       hAxes   = obj.hAxes;
%       nA      = numel(hAxes);      
%       
%       pFrame  = pixelPosition(hFrame);
%       axesMap = zeros(pFrame([3 4])+1);
%       
%       for m = 1:nA
%         hAxis = hAxes{m};
%         pAxis = round(pixelPosition(hAxis))+1;
%         try
%           axesMap(pAxis(1):pAxis(1)+pAxis(3), pAxis(2):pAxis(2)+pAxis(4)) = m;
%         catch err
%           disp(err);
%         end
%       end
%       
%       obj.mAxes = axesMap;
    end
    
    function validateAxes(obj)
      %       newAxes = {};
      %       childAxes = get(obj.hFrame, 'Children');
      %       for hAxis = obj.hAxes
      %         validHandle   = false;
      %
      %         try validHandle = ishandle(hAxis); end
      %
      %         if validHandle && any(childAxes==hAxis)
      %           newAxes{end+1} = hAxis;
      %         end
      %       end
      %       obj.hAxes = newAxes;
    end
    
    function delete(obj)
      
      jComponents = obj.jComponents;
      
      for j = 1:numel(jComponents)
        jObject = jComponents{j};
        %try jObject.dispose; end
        % try jObject = 0; end
        try delete(jObject); end
      end
      
      obj.jComponents = {};
      
      jReferences = obj.jReferences;
      
      for m = 1:numel(jReferences)
        try obj.(jReferences{m}) = []; end
      end
      
      obj.jReferences = {};
      
      jComponents = {}; jReferences = {};
      
      try delete(gcf); end
      
    end
    
  end
  
end
