classdef PlotMediator < Grasppe.Core.Mediator
  %PLOTMEDIATOR Summary of this class goes here
  %   Detailed explanation goes here
  
  properties (GetObservable, SetObservable)
    CASEID
    SETID
    SHEETID
    VIEW
    PLOTCOLOR
  end
  
  
  properties (Hidden)
    Cases = {'ritsm7402a', 'ritsm7402b', 'ritsm7402c', 'rithp7k01', 'rithp5501'};
    Sets  = int8([100, 75, 50, 25, 0]);
    
    CaseIDControl
    SetIDControl
    CommandPromptControl;
    
    MediatorControls;
    
    PlotFigure  = [];
    DataSources = {};
    PlotObjects = {};    
  end
  
  methods
    function obj = PlotMediator()
      obj = obj@Grasppe.Core.Mediator;
    end
    
    function value = get.CASEID(obj)
      value = [];
      try value = obj.CaseID; end
    end
    
    function set.CASEID(obj, value)
      try obj.CaseID = value; end
      obj.updateControls;
    end
    
    function value = get.SETID(obj)
      value = [];
      try value = obj.SetID; end
    end
    
    function set.SETID(obj, value)
      try obj.SetID = value; end
      obj.updateControls;
    end
    
    function value = get.SHEETID(obj)
      value = [];
      try value = obj.SheetID; end
    end
    
    function set.SHEETID(obj, value)
      try obj.SheetID = value; end
      % obj.updateControls;
    end
    
    
    function value = get.VIEW(obj)
      value = [];
      try value = obj.View; end
    end
    
    function set.VIEW(obj, value)
      try obj.View = value; end
    end
    
    
    function value = get.PLOTCOLOR(obj)
      value = [];
      try value = obj.PlotColor; end
    end
    
    function set.PLOTCOLOR(obj, value)
      try obj.PlotColor = value; end
    end
    
    %     function delete(obj)
    %       controlEntries  = obj.MediatorControls;
    %       obj.delete@Grasppe.Core.Mediator;
    %     end
    
    function updateControls(obj)
      disp('Updating Controls');
      if ~isempty(obj.CaseIDControl)
        try selectedCase = find(strcmpi(obj.CaseID, obj.Cases));
          if ~isempty(selectedCase)
            obj.CaseIDControl.setSelectedIndex(selectedCase-1);
          end
        end
      end
      
      if ~isempty(obj.SetIDControl)
        try selectedSet = find(obj.SetID == obj.Sets, 1);
          if ~isempty(selectedSet)
            obj.SetIDControl.setSelectedIndex(selectedSet-1);
          end
        end
      end
      
    end
    
    function createControls(obj, parentFigure)
      
      cases     = obj.Cases; sets      = obj.Sets;
      
      hFigure   = parentFigure.Handle;
      
      selectedCase = [];
      try selectedCase = find(strcmpi(obj.CaseID, cases)); end
      jCaseMenu = obj.createDropDown(hFigure, cases, selectedCase, ...
        @obj.selectCaseID, [], [], 150);
      obj.CaseIDControl = jCaseMenu;
      
      selectedSet = [];
      try selectedSet = find(sets==obj.SetID); end
      jSetMenu = obj.createDropDown(hFigure, sets, selectedSet, ...
        @obj.selectSetID, [], [], 75);
      obj.SetIDControl = jSetMenu;
      
      jCommandPrompt = obj.createCommandPrompt(hFigure);
      obj.CommandPromptControl = jCommandPrompt;
      
      hToolbar = findall(allchild(hFigure),'flat','type','uitoolbar');
      
      if isempty(hToolbar), hToolbar  = uitoolbar(hFigure); end
      
      drawnow;
      
      jContainer = get(hToolbar(1),'JavaContainer');
      jToolbar = jContainer.getComponentPeer;
      
      jToolbar.add(jCaseMenu);
      jToolbar.add(jSetMenu);
      jToolbar.add(jCommandPrompt);
      jToolbar.repaint;
      jToolbar.revalidate;
      
      refresh(hFigure);
      
    end
    
    function selectCaseID(obj, source, event)
      % disp(source); caseID = source.getSelectedItem;
      try obj.CaseID = source.getSelectedItem; end
    end
    
    function selectSetID(obj, source, event)
      % disp(source); setID = source.getSelectedItem;
      try obj.SetID = source.getSelectedItem; end
    end
    
    function jText = createCommandPrompt(obj, hFigure, left, bottom, width, height)
      
      object = javax.swing.JTextField();
      [jText, hText] = javacomponent(object);
      
      jText.KeyPressedCallback  = @obj.commandPromptCallback;
      jText.KeyTypedCallback    = @obj.commandPromptCallback;
      
      p = get(hText, 'Position');
      try if ~isempty(left),    p(1) = left;  end; end
      try if ~isempty(bottom),  p(2) = bottom;
        else p(2) = -height-10; end; end
      try if ~isempty(width),   p(3) = width; end; end
      try if ~isempty(height),  p(4) = height; end; end
      set(hText, 'Position', p);
      
      try
        if ~isempty(width),
          s         = jText.getMaximumSize;
          s.width   = width;
          jText.setMaximumSize(s);
        end
      end
      
    end
    
    function commandPromptCallback(obj, source, event)
      disp(event);
      
      import com.mathworks.mlservices.MLCommandHistoryServices;
      import com.mathworks.mlservices.MLExecuteServices;
      
      
      persistent historyStep;
      persistent localCommands;
      
      if isempty(historyStep),    historyStep = 0; end
      if ~iscell(localCommands),  localCommands = {}; end
      
      pressed   = false; try pressed  = isequal(event.getID, event.KEY_PRESSED);  end
      released  = false; try released = isequal(event.getID, event.KEY_RELEASED); end
      typed     = false; try typed    = isequal(event.getID, event.KEY_TYPED);    end
      
      stepped   = false;
      
      
      switch event.getKeyCode
        case 38
          % Up    [KEY_PRESSED,keyCode=38,keyText=,keyChar=Undefined keyChar,keyLocation=KEY_LOCATION_STANDARD,rawCode=0,primaryLevelUnicode=0,scancode=0] on javax.swing.JTextField[,209,0,1183x26,layout=javax.swing.plaf.basic.BasicTextUI$UpdateHandler,alignmentX=0.0,alignmentY=0.0,border=com.apple.laf.AquaTextFieldBorder@77c2c9af,flags=16777512,maximumSize=,minimumSize=,preferredSize=,caretColor=javax.swing.plaf.ColorUIResource[r=0,g=0,b=0],disabledTextColor=javax.swing.plaf.ColorUIResource[r=128,g=128,b=128],editable=true,margin=javax.swing.plaf.InsetsUIResource[top=0,left=0,bottom=0,right=0],selectedTextColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=0,g=0,b=0],selectionColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=167,g=202,b=255],columns=0,columnWidth=0,command=,horizontalAlignment=LEADING]
          if pressed
            historyStep = historyStep + 1;
            stepped = true;
          end
        case 40
          if pressed
            historyStep = historyStep - 1;
            stepped = true;
          end
          % Down  [KEY_PRESSED,keyCode=40,keyText=,keyChar=Undefined keyChar,keyLocation=KEY_LOCATION_STANDARD,rawCode=0,primaryLevelUnicode=0,scancode=0] on javax.swing.JTextField[,209,0,1183x26,layout=javax.swing.plaf.basic.BasicTextUI$UpdateHandler,alignmentX=0.0,alignmentY=0.0,border=com.apple.laf.AquaTextFieldBorder@77c2c9af,flags=16777512,maximumSize=,minimumSize=,preferredSize=,caretColor=javax.swing.plaf.ColorUIResource[r=0,g=0,b=0],disabledTextColor=javax.swing.plaf.ColorUIResource[r=128,g=128,b=128],editable=true,margin=javax.swing.plaf.InsetsUIResource[top=0,left=0,bottom=0,right=0],selectedTextColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=0,g=0,b=0],selectionColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=167,g=202,b=255],columns=0,columnWidth=0,command=,horizontalAlignment=LEADING]
          
        case 10
          % Enter [KEY_PRESSED,keyCode=10,keyText=,keyChar=,keyLocation=KEY_LOCATION_STANDARD,rawCode=0,primaryLevelUnicode=0,scancode=0] on javax.swing.JTextField[,209,0,1183x26,layout=javax.swing.plaf.basic.BasicTextUI$UpdateHandler,alignmentX=0.0,alignmentY=0.0,border=com.apple.laf.AquaTextFieldBorder@77c2c9af,flags=16777512,maximumSize=,minimumSize=,preferredSize=,caretColor=javax.swing.plaf.ColorUIResource[r=0,g=0,b=0],disabledTextColor=javax.swing.plaf.ColorUIResource[r=128,g=128,b=128],editable=true,margin=javax.swing.plaf.InsetsUIResource[top=0,left=0,bottom=0,right=0],selectedTextColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=0,g=0,b=0],selectionColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=167,g=202,b=255],columns=0,columnWidth=0,command=,horizontalAlignment=LEADING]
          %       [KEY_TYPED,keyCode=0,keyText=Unknown keyCode: 0x0,keyChar=,keyLocation=KEY_LOCATION_UNKNOWN,rawCode=0,primaryLevelUnicode=0,scancode=0] on javax.swing.JTextField[,209,0,1183x26,layout=javax.swing.plaf.basic.BasicTextUI$UpdateHandler,alignmentX=0.0,alignmentY=0.0,border=com.apple.laf.AquaTextFieldBorder@77c2c9af,flags=16777512,maximumSize=,minimumSize=,preferredSize=,caretColor=javax.swing.plaf.ColorUIResource[r=0,g=0,b=0],disabledTextColor=javax.swing.plaf.ColorUIResource[r=128,g=128,b=128],editable=true,margin=javax.swing.plaf.InsetsUIResource[top=0,left=0,bottom=0,right=0],selectedTextColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=0,g=0,b=0],selectionColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=167,g=202,b=255],columns=0,columnWidth=0,command=,horizontalAlignment=LEADING]
          if pressed
            historyStep = 0;
            command = char(source.getText);
            
            if strfind(command,'~')==1
              obj.executeCommand(command(2:end));
            else
              try MLExecuteServices.consoleEval(command); end
            end   
            
            localCommands{end+1} = command;
            source.setText('');
          end
          
        case 27
          % Esc 	[KEY_PRESSED,keyCode=27,keyText=,keyChar=,keyLocation=KEY_LOCATION_STANDARD,rawCode=0,primaryLevelUnicode=0,scancode=0] on javax.swing.JTextField[,209,0,1183x26,layout=javax.swing.plaf.basic.BasicTextUI$UpdateHandler,alignmentX=0.0,alignmentY=0.0,border=com.apple.laf.AquaTextFieldBorder@77c2c9af,flags=16777576,maximumSize=,minimumSize=,preferredSize=,caretColor=javax.swing.plaf.ColorUIResource[r=0,g=0,b=0],disabledTextColor=javax.swing.plaf.ColorUIResource[r=128,g=128,b=128],editable=true,margin=javax.swing.plaf.InsetsUIResource[top=0,left=0,bottom=0,right=0],selectedTextColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=0,g=0,b=0],selectionColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=167,g=202,b=255],columns=0,columnWidth=0,command=,horizontalAlignment=LEADING]
          if pressed
            source.setText('');
            historyStep = 0;
          end
          
        otherwise
          % a     [KEY_PRESSED,keyCode=65,keyText=A,keyChar='a',keyLocation=KEY_LOCATION_STANDARD,rawCode=0,primaryLevelUnicode=0,scancode=0] on javax.swing.JTextField[,209,0,1183x26,layout=javax.swing.plaf.basic.BasicTextUI$UpdateHandler,alignmentX=0.0,alignmentY=0.0,border=com.apple.laf.AquaTextFieldBorder@77c2c9af,flags=16777512,maximumSize=,minimumSize=,preferredSize=,caretColor=javax.swing.plaf.ColorUIResource[r=0,g=0,b=0],disabledTextColor=javax.swing.plaf.ColorUIResource[r=128,g=128,b=128],editable=true,margin=javax.swing.plaf.InsetsUIResource[top=0,left=0,bottom=0,right=0],selectedTextColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=0,g=0,b=0],selectionColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=167,g=202,b=255],columns=0,columnWidth=0,command=,horizontalAlignment=LEADING]
          %       [KEY_TYPED,keyCode=0,keyText=Unknown keyCode: 0x0,keyChar='a',keyLocation=KEY_LOCATION_UNKNOWN,rawCode=0,primaryLevelUnicode=0,scancode=0] on javax.swing.JTextField[,209,0,1183x26,layout=javax.swing.plaf.basic.BasicTextUI$UpdateHandler,alignmentX=0.0,alignmentY=0.0,border=com.apple.laf.AquaTextFieldBorder@77c2c9af,flags=16785704,maximumSize=,minimumSize=,preferredSize=,caretColor=javax.swing.plaf.ColorUIResource[r=0,g=0,b=0],disabledTextColor=javax.swing.plaf.ColorUIResource[r=128,g=128,b=128],editable=true,margin=javax.swing.plaf.InsetsUIResource[top=0,left=0,bottom=0,right=0],selectedTextColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=0,g=0,b=0],selectionColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=167,g=202,b=255],columns=0,columnWidth=0,command=,horizontalAlignment=LEADING]
          
          % s     [KEY_PRESSED,keyCode=83,keyText=S,keyChar='s',keyLocation=KEY_LOCATION_STANDARD,rawCode=0,primaryLevelUnicode=0,scancode=0] on javax.swing.JTextField[,209,0,1183x26,layout=javax.swing.plaf.basic.BasicTextUI$UpdateHandler,alignmentX=0.0,alignmentY=0.0,border=com.apple.laf.AquaTextFieldBorder@77c2c9af,flags=16777512,maximumSize=,minimumSize=,preferredSize=,caretColor=javax.swing.plaf.ColorUIResource[r=0,g=0,b=0],disabledTextColor=javax.swing.plaf.ColorUIResource[r=128,g=128,b=128],editable=true,margin=javax.swing.plaf.InsetsUIResource[top=0,left=0,bottom=0,right=0],selectedTextColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=0,g=0,b=0],selectionColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=167,g=202,b=255],columns=0,columnWidth=0,command=,horizontalAlignment=LEADING]
          %       [KEY_TYPED,keyCode=0,keyText=Unknown keyCode: 0x0,keyChar='s',keyLocation=KEY_LOCATION_UNKNOWN,rawCode=0,primaryLevelUnicode=0,scancode=0] on javax.swing.JTextField[,209,0,1183x26,layout=javax.swing.plaf.basic.BasicTextUI$UpdateHandler,alignmentX=0.0,alignmentY=0.0,border=com.apple.laf.AquaTextFieldBorder@77c2c9af,flags=16785704,maximumSize=,minimumSize=,preferredSize=,caretColor=javax.swing.plaf.ColorUIResource[r=0,g=0,b=0],disabledTextColor=javax.swing.plaf.ColorUIResource[r=128,g=128,b=128],editable=true,margin=javax.swing.plaf.InsetsUIResource[top=0,left=0,bottom=0,right=0],selectedTextColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=0,g=0,b=0],selectionColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=167,g=202,b=255],columns=0,columnWidth=0,command=,horizontalAlignment=LEADING]
          
          % ^a    [KEY_PRESSED,keyCode=16,keyText=,keyChar=Undefined keyChar,modifiers=,extModifiers=,keyLocation=KEY_LOCATION_RIGHT,rawCode=0,primaryLevelUnicode=0,scancode=0] on javax.swing.JTextField[,209,0,1183x26,layout=javax.swing.plaf.basic.BasicTextUI$UpdateHandler,alignmentX=0.0,alignmentY=0.0,border=com.apple.laf.AquaTextFieldBorder@77c2c9af,flags=16777512,maximumSize=,minimumSize=,preferredSize=,caretColor=javax.swing.plaf.ColorUIResource[r=0,g=0,b=0],disabledTextColor=javax.swing.plaf.ColorUIResource[r=128,g=128,b=128],editable=true,margin=javax.swing.plaf.InsetsUIResource[top=0,left=0,bottom=0,right=0],selectedTextColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=0,g=0,b=0],selectionColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=167,g=202,b=255],columns=0,columnWidth=0,command=,horizontalAlignment=LEADING]
          %       [KEY_PRESSED,keyCode=65,keyText=A,keyChar='A',modifiers=,extModifiers=,keyLocation=KEY_LOCATION_STANDARD,rawCode=0,primaryLevelUnicode=0,scancode=0] on javax.swing.JTextField[,209,0,1183x26,invalid,layout=javax.swing.plaf.basic.BasicTextUI$UpdateHandler,alignmentX=0.0,alignmentY=0.0,border=com.apple.laf.AquaTextFieldBorder@77c2c9af,flags=16777512,maximumSize=,minimumSize=,preferredSize=,caretColor=javax.swing.plaf.ColorUIResource[r=0,g=0,b=0],disabledTextColor=javax.swing.plaf.ColorUIResource[r=128,g=128,b=128],editable=true,margin=javax.swing.plaf.InsetsUIResource[top=0,left=0,bottom=0,right=0],selectedTextColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=0,g=0,b=0],selectionColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=167,g=202,b=255],columns=0,columnWidth=0,command=,horizontalAlignment=LEADING]
          %       [KEY_TYPED,keyCode=0,keyText=Unknown keyCode: 0x0,keyChar='A',modifiers=,extModifiers=,keyLocation=KEY_LOCATION_UNKNOWN,rawCode=0,primaryLevelUnicode=0,scancode=0] on javax.swing.JTextField[,209,0,1183x26,layout=javax.swing.plaf.basic.BasicTextUI$UpdateHandler,alignmentX=0.0,alignmentY=0.0,border=com.apple.laf.AquaTextFieldBorder@77c2c9af,flags=16785704,maximumSize=,minimumSize=,preferredSize=,caretColor=javax.swing.plaf.ColorUIResource[r=0,g=0,b=0],disabledTextColor=javax.swing.plaf.ColorUIResource[r=128,g=128,b=128],editable=true,margin=javax.swing.plaf.InsetsUIResource[top=0,left=0,bottom=0,right=0],selectedTextColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=0,g=0,b=0],selectionColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=167,g=202,b=255],columns=0,columnWidth=0,command=,horizontalAlignment=LEADING]
          
          % ^s    [KEY_PRESSED,keyCode=16,keyText=,keyChar=Undefined keyChar,modifiers=,extModifiers=,keyLocation=KEY_LOCATION_RIGHT,rawCode=0,primaryLevelUnicode=0,scancode=0] on javax.swing.JTextField[,209,0,1183x26,layout=javax.swing.plaf.basic.BasicTextUI$UpdateHandler,alignmentX=0.0,alignmentY=0.0,border=com.apple.laf.AquaTextFieldBorder@77c2c9af,flags=16777512,maximumSize=,minimumSize=,preferredSize=,caretColor=javax.swing.plaf.ColorUIResource[r=0,g=0,b=0],disabledTextColor=javax.swing.plaf.ColorUIResource[r=128,g=128,b=128],editable=true,margin=javax.swing.plaf.InsetsUIResource[top=0,left=0,bottom=0,right=0],selectedTextColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=0,g=0,b=0],selectionColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=167,g=202,b=255],columns=0,columnWidth=0,command=,horizontalAlignment=LEADING]
          %       [KEY_PRESSED,keyCode=83,keyText=S,keyChar='S',modifiers=,extModifiers=,keyLocation=KEY_LOCATION_STANDARD,rawCode=0,primaryLevelUnicode=0,scancode=0] on javax.swing.JTextField[,209,0,1183x26,invalid,layout=javax.swing.plaf.basic.BasicTextUI$UpdateHandler,alignmentX=0.0,alignmentY=0.0,border=com.apple.laf.AquaTextFieldBorder@77c2c9af,flags=16777512,maximumSize=,minimumSize=,preferredSize=,caretColor=javax.swing.plaf.ColorUIResource[r=0,g=0,b=0],disabledTextColor=javax.swing.plaf.ColorUIResource[r=128,g=128,b=128],editable=true,margin=javax.swing.plaf.InsetsUIResource[top=0,left=0,bottom=0,right=0],selectedTextColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=0,g=0,b=0],selectionColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=167,g=202,b=255],columns=0,columnWidth=0,command=,horizontalAlignment=LEADING]
          %       [KEY_TYPED,keyCode=0,keyText=Unknown keyCode: 0x0,keyChar='S',modifiers=,extModifiers=,keyLocation=KEY_LOCATION_UNKNOWN,rawCode=0,primaryLevelUnicode=0,scancode=0] on javax.swing.JTextField[,209,0,1183x26,layout=javax.swing.plaf.basic.BasicTextUI$UpdateHandler,alignmentX=0.0,alignmentY=0.0,border=com.apple.laf.AquaTextFieldBorder@77c2c9af,flags=16785704,maximumSize=,minimumSize=,preferredSize=,caretColor=javax.swing.plaf.ColorUIResource[r=0,g=0,b=0],disabledTextColor=javax.swing.plaf.ColorUIResource[r=128,g=128,b=128],editable=true,margin=javax.swing.plaf.InsetsUIResource[top=0,left=0,bottom=0,right=0],selectedTextColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=0,g=0,b=0],selectionColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=167,g=202,b=255],columns=0,columnWidth=0,command=,horizontalAlignment=LEADING]
          
          % ~a    [KEY_PRESSED,keyCode=18,keyText=,keyChar=Undefined keyChar,modifiers=,extModifiers=,keyLocation=KEY_LOCATION_LEFT,rawCode=0,primaryLevelUnicode=0,scancode=0] on javax.swing.JTextField[,209,0,1183x26,layout=javax.swing.plaf.basic.BasicTextUI$UpdateHandler,alignmentX=0.0,alignmentY=0.0,border=com.apple.laf.AquaTextFieldBorder@77c2c9af,flags=16777512,maximumSize=,minimumSize=,preferredSize=,caretColor=javax.swing.plaf.ColorUIResource[r=0,g=0,b=0],disabledTextColor=javax.swing.plaf.ColorUIResource[r=128,g=128,b=128],editable=true,margin=javax.swing.plaf.InsetsUIResource[top=0,left=0,bottom=0,right=0],selectedTextColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=0,g=0,b=0],selectionColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=167,g=202,b=255],columns=0,columnWidth=0,command=,horizontalAlignment=LEADING]
          %       [KEY_PRESSED,keyCode=65,keyText=A,keyChar='å',modifiers=,extModifiers=,keyLocation=KEY_LOCATION_STANDARD,rawCode=0,primaryLevelUnicode=0,scancode=0] on javax.swing.JTextField[,209,0,1183x26,invalid,layout=javax.swing.plaf.basic.BasicTextUI$UpdateHandler,alignmentX=0.0,alignmentY=0.0,border=com.apple.laf.AquaTextFieldBorder@77c2c9af,flags=16777576,maximumSize=,minimumSize=,preferredSize=,caretColor=javax.swing.plaf.ColorUIResource[r=0,g=0,b=0],disabledTextColor=javax.swing.plaf.ColorUIResource[r=128,g=128,b=128],editable=true,margin=javax.swing.plaf.InsetsUIResource[top=0,left=0,bottom=0,right=0],selectedTextColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=0,g=0,b=0],selectionColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=167,g=202,b=255],columns=0,columnWidth=0,command=,horizontalAlignment=LEADING]
          %       [KEY_TYPED,keyCode=0,keyText=Unknown keyCode: 0x0,keyChar='å',modifiers=,extModifiers=,keyLocation=KEY_LOCATION_UNKNOWN,rawCode=0,primaryLevelUnicode=0,scancode=0] on javax.swing.JTextField[,209,0,1183x26,layout=javax.swing.plaf.basic.BasicTextUI$UpdateHandler,alignmentX=0.0,alignmentY=0.0,border=com.apple.laf.AquaTextFieldBorder@77c2c9af,flags=16785768,maximumSize=,minimumSize=,preferredSize=,caretColor=javax.swing.plaf.ColorUIResource[r=0,g=0,b=0],disabledTextColor=javax.swing.plaf.ColorUIResource[r=128,g=128,b=128],editable=true,margin=javax.swing.plaf.InsetsUIResource[top=0,left=0,bottom=0,right=0],selectedTextColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=0,g=0,b=0],selectionColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=167,g=202,b=255],columns=0,columnWidth=0,command=,horizontalAlignment=LEADING]
          
          % ~s    [KEY_PRESSED,keyCode=18,keyText=,keyChar=Undefined keyChar,modifiers=,extModifiers=,keyLocation=KEY_LOCATION_LEFT,rawCode=0,primaryLevelUnicode=0,scancode=0] on javax.swing.JTextField[,209,0,1183x26,layout=javax.swing.plaf.basic.BasicTextUI$UpdateHandler,alignmentX=0.0,alignmentY=0.0,border=com.apple.laf.AquaTextFieldBorder@77c2c9af,flags=16777512,maximumSize=,minimumSize=,preferredSize=,caretColor=javax.swing.plaf.ColorUIResource[r=0,g=0,b=0],disabledTextColor=javax.swing.plaf.ColorUIResource[r=128,g=128,b=128],editable=true,margin=javax.swing.plaf.InsetsUIResource[top=0,left=0,bottom=0,right=0],selectedTextColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=0,g=0,b=0],selectionColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=167,g=202,b=255],columns=0,columnWidth=0,command=,horizontalAlignment=LEADING]
          %       [KEY_PRESSED,keyCode=83,keyText=S,keyChar='ß',modifiers=,extModifiers=,keyLocation=KEY_LOCATION_STANDARD,rawCode=0,primaryLevelUnicode=0,scancode=0] on javax.swing.JTextField[,209,0,1183x26,invalid,layout=javax.swing.plaf.basic.BasicTextUI$UpdateHandler,alignmentX=0.0,alignmentY=0.0,border=com.apple.laf.AquaTextFieldBorder@77c2c9af,flags=16777576,maximumSize=,minimumSize=,preferredSize=,caretColor=javax.swing.plaf.ColorUIResource[r=0,g=0,b=0],disabledTextColor=javax.swing.plaf.ColorUIResource[r=128,g=128,b=128],editable=true,margin=javax.swing.plaf.InsetsUIResource[top=0,left=0,bottom=0,right=0],selectedTextColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=0,g=0,b=0],selectionColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=167,g=202,b=255],columns=0,columnWidth=0,command=,horizontalAlignment=LEADING]
          %       [KEY_TYPED,keyCode=0,keyText=Unknown keyCode: 0x0,keyChar='ß',modifiers=,extModifiers=,keyLocation=KEY_LOCATION_UNKNOWN,rawCode=0,primaryLevelUnicode=0,scancode=0] on javax.swing.JTextField[,209,0,1183x26,layout=javax.swing.plaf.basic.BasicTextUI$UpdateHandler,alignmentX=0.0,alignmentY=0.0,border=com.apple.laf.AquaTextFieldBorder@77c2c9af,flags=16785768,maximumSize=,minimumSize=,preferredSize=,caretColor=javax.swing.plaf.ColorUIResource[r=0,g=0,b=0],disabledTextColor=javax.swing.plaf.ColorUIResource[r=128,g=128,b=128],editable=true,margin=javax.swing.plaf.InsetsUIResource[top=0,left=0,bottom=0,right=0],selectedTextColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=0,g=0,b=0],selectionColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=167,g=202,b=255],columns=0,columnWidth=0,command=,horizontalAlignment=LEADING]
          return;
      end
      
      if stepped && historyStep > 0
        if historyStep <= numel(localCommands)
          source.setText(char(localCommands{end+1-historyStep}));
        else
          history   = MLCommandHistoryServices.getSessionHistory;
          nHistory  = numel(history);
          sHistory  = nHistory+1+numel(localCommands)-historyStep
          if sHistory > nHistory, sHistory  = nHistory; end
          if sHistory < 0,        sHistory  = 0;        end
          if sHistory > 0,
            source.setText(char(history(sHistory)));
          else
            source.setText('');
          end
        end
      else
        source.setText('');
      end
      
    end
    
    function executeCommand(obj, command)
      try eval(command); end
    end
    
    
    function [cmdWin jTextarea] = getCommandWindow(obj)
      jDesktop = com.mathworks.mde.desk.MLDesktop.getInstance;
      cmdWin=jDesktop.getClient('Command Window');
      if nargout>1, jTextarea = cmdWin.getComponent(0).getViewport.getView; end
    end
    
    function jCombo = createDropDown(obj, hFigure, options, selection, callback, left, bottom, width, height)
      % options = {'opt #1', 'opt #2', 'opt #3'};
      
      try if isnumeric(options)
          options = num2cell(options);
        end; end;
      
      combo = javax.swing.JComboBox(options);
      % combo.setEditable(1);
      %[jCombo,hCombo] = javacomponent(combo, position);
      [jCombo,hCombo] = javacomponent(combo);
      
      jCombo.ActionPerformedCallback = callback;
      
      p = get(hCombo, 'Position');
      try if ~isempty(left),    p(1) = left;  end; end
      try if ~isempty(bottom),  p(2) = bottom;
        else p(2) = -height-10; end; end
      try if ~isempty(width),   p(3) = width; end; end
      try if ~isempty(height),  p(4) = height; end; end
      set(hCombo, 'Position', p);
      
      
      try
        if ~isempty(width),
          s         = jCombo.getMaximumSize;
          s.width   = width;
          jCombo.setMaximumSize(s);
        end
      end
      
      
      
      try if ~isempty(selection), jCombo.setSelectedIndex(selection-1); end; end
      
      obj.registerControl(jCombo, hCombo, combo);
      
    end
    
    function registerControl(obj, c, h, j)
      entry = struct('Component', c, 'Handle', h, 'Object', j);
      
      if isempty(obj.MediatorControls)
        obj.MediatorControls = entry;
      else
        obj.MediatorControls(end+1) = entry;
      end
      
      try obj.registerHandle(h); end
      try obj.registerHandle(c); end
      try obj.registerHandle(j); end
      
    end
    
  end
  
end

