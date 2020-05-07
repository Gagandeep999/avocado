% PROGRAM START
           entry
% processing t1 stores 1
           addi R1, R0, 1
           sw lit_t1(R0), R1
% processing x = 1
           lw R1, lit_t1(R0)
           sw var_x(R0), R1
% processing t2 stores 2
           addi R2, R0, 2
           sw lit_t2(R0), R2
% processing x comparison 2
           lw R3, var_x(R0)
           lw R4, lit_t2(R0)
           ceq R2, R3, R4
           sw temp_t3(R0), R2
% processing if condition 
if1           lw R1, temp_t3(R0)
           bz R1, if1_else 
% processing t4 stores 1
           addi R1, R0, 1
           sw lit_t4(R0), R1
% processing x add/sub/or 1
           lw R2, var_x(R0)
           lw R4, lit_t4(R0)
           add R1, R2, R4
           sw temp_t5(R0), R1
% writing to console
           lw R1, temp_t5(R0)
           jl R15, putint
           j if1_end 
if1_else           nop
% processing t6 stores 1
           addi R1, R0, 1
           sw lit_t6(R0), R1
% processing x add/sub/or 1
           lw R4, var_x(R0)
           lw R2, lit_t6(R0)
           sub R1, R4, R2
           sw temp_t7(R0), R1
% writing to console
           lw R1, temp_t7(R0)
           jl R15, putint
if1_end           nop
           hlt
% PROGRAM END

% space for var_x
var_x res 4 
% space for constant 1
lit_t1 res 4 
% space for constant 2
lit_t2 res 4 
% space for temp_t3
temp_t3 res 4
% space for constant 1
lit_t4 res 4 
% space for temp_t5
temp_t5 res 4
% space for constant 1
lit_t6 res 4 
% space for temp_t7
temp_t7 res 4
