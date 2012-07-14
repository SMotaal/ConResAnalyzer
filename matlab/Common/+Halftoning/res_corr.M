
% Function to compute the correlation matrix of the residual w.r.t original
% RHO = RES_CORR(IN,RES) 
% computes the correlation matrix of the residual color image RES
% w.r.t original IN

% Vishal Monga, July 2002

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.

function rho = res_corr(in,res)

in1=in(:,:,1);res1=res(:,:,1);
in2=in(:,:,2);res2=res(:,:,2);
in3=in(:,:,3);res3=res(:,:,3);

rho(1,1) = corr(in1,res1);
rho(1,2) = corr(in1,res2);
rho(1,3) = corr(in1,res3);
rho(2,1) = corr(in2,res1);
rho(2,2) = corr(in2,res2);
rho(2,3) = corr(in2,res3);
rho(3,1) = corr(in3,res1);
rho(3,2) = corr(in3,res2);
rho(3,3) = corr(in3,res3);

