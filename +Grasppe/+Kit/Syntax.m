classdef Syntax
  %DEFAULTS Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    IMPORTS = {'Grasppe.Patterns.*' , 'Grasppe.Kit.*'};

%     CONTRAST_RANGE    = [100 70.17 49.239 34.551 24.245 17.013 11.938 8.377 5.878 4.125 2.894 2.031 1.425 1.0];

  end
  
  methods (Access=private)
    
    function obj = Syntax()
    end
    
  end
  
  methods (Static)
    
    
    
  end
  methods (Static)
    
    function ShowHTML(in)
      mytext  = in; % '<html><body><table border="1"><tr><th>Month</th><th>Savings</th></tr><tr><td>January</td><td>$100</td></tr></table></body></html>';

      hfig    = figure();
      je      = javax.swing.JEditorPane( 'text/html', mytext );
      jp      = javax.swing.JScrollPane( je );

      [hcomponent, hcontainer] = javacomponent( jp, [], hfig );
      set( hcontainer, 'units', 'normalized', 'position', [0,0,1,1] );

      %# Turn anti-aliasing on ( R2006a, java 5.0 )
      java.lang.System.setProperty( 'awt.useSystemAAFontSettings', 'on' );
      je.putClientProperty( javax.swing.JEditorPane.HONOR_DISPLAY_PROPERTIES, true );
      % je.putClientProperty( com.sun.java.swing.SwingUtilities2.AA_TEXT_PROPERTY_KEY, true );

      je.setFont( java.awt.Font( 'Arial', java.awt.Font.PLAIN, 13 ) );
      
    end
    
    function [out] = Highlight(in)
      
      s = warning('off', 'all');      
      
      [filePath fileName fileExt] = fileparts(eval(FILE));
      
      %% Read Input
      try
      if ischar(in) && (exist(in, 'file')>0)
        mCode = fileread(in);
      else
        mCode = in;
      end
      
      tempFile = tempname; % fullfile(filePath, 'output');
      mCodeFile = [tempFile '.m'];
      mHtmlFile = [tempFile '.html'];
      
      fid = fopen(mCodeFile, 'wt+');
        fprintf(fid,'%s',mCode);
      fclose(fid);
      
      catch ex
        rethrow(ex);
        return;
      end
      
      %% Read Template
      tHtmlFile = fullfile(filePath, 'template', 'highlight.html');
      
      tHtml = fileread(tHtmlFile);
      
      tParts = regexp(tHtml, '#Body#', 'split');
      
      %% Generate Output
      
      mOptions.type = 'html';
      mOptions.tabs = 4;
      mOptions.linenb = 0;
      mOptions.indent = 4;
      
      try
        fid = fopen(mHtmlFile, 'wt+');
          fprintf(fid, '%s', tParts{1});
          highlight(mCodeFile, mOptions, fid);
          fprintf(fid, '%s', tParts{2});
        fclose(fid);
        mHtml = fileread(mHtmlFile);    
      catch ex
        mHtml = mCode;
      end
      

      try delete mCodeFile; end
      try delete mHtmlFile; end
      
      out = mHtml;
      
      warning(s);
      
    end
    
  end
  
  methods (Static)  % Getters
%     function value = ContrastRange()
%       Grasppe.Kit.Syntax.GetInstance;
%       value     = Instance.CONTRAST_RANGE;
%     end
    
    
    function [instance class] = GetInstance()
      persistent Instance; ...
        Class = eval(CLASS);
      
      Instance = eval(Grasppe.Kit.GetInstance); ...
        instance = Instance; ...
        class    = Class;
      
      if nargout == 0
        assignin('caller', 'Instance', Instance);
      end
    end
    
    function [imports] = GetImports
      eval([eval(CLASS) '.GetInstance;']);
      
      imports = Instance.IMPORTS;
      
      assignin('caller', 'Imports', imports);
      
      % for m = 1:numel(imports)
      %   evalin('caller', ['import(''' imports{m} ''');']);
      % end
      % evalin('caller', 'import(''Imports{:}'');');
    end
    
  end    
    
  
end

