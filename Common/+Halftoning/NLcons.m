%NLcons.m
%Nonlinear constraints
% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.

function [Con,Ceq] = NLcons(X,C,d)

Con = -1;%always satisfied
Ceq(1) = abs(X(1))+abs(X(2))+abs(X(3))+abs(X(10))+abs(X(11))+abs(X(12))+...
         abs(X(19))+abs(X(20))+abs(X(21))+abs(X(28))+abs(X(29))+abs(X(30))-1;
     
Ceq(2) = abs(X(4))+abs(X(5))+abs(X(6))+abs(X(13))+abs(X(14))+abs(X(15))+...
         abs(X(22))+abs(X(23))+abs(X(24))+abs(X(31))+abs(X(32))+abs(X(33))-1;

Ceq(3) = abs(X(7))+abs(X(8))+abs(X(9))+abs(X(16))+abs(X(17))+abs(X(18))+...
         abs(X(25))+abs(X(26))+abs(X(27))+abs(X(34))+abs(X(35))+abs(X(36))-1;
          