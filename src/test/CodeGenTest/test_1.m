% PROGRAM START
           entry
% processing t1 stores 5
           addi R1, R0, 5
           sw lit_t1(R0), R1
% processing b = 5
           lw R1, lit_t1(R0)
           sw var_b(R0), R1
% processing t2 stores 5
           addi R1, R0, 5
           sw lit_t2(R0), R1
% processing a = 5
           lw R1, lit_t2(R0)
           sw var_a(R0), R1
% processing a + a
           lw R2, var_a(R0)
           lw R3, var_a(R0)
           add R1, R2, R3
           sw temp_t3(R0), R1
% processing result = +
           lw R1, temp_t3(R0)
           sw var_result(R0), R1
% processing a + a
           lw R3, var_a(R0)
           lw R2, var_a(R0)
           mul R1, R3, R2
           sw temp_t4(R0), R1
% processing result = *
           lw R1, temp_t4(R0)
           sw var_result(R0), R1
% processing a + a
           lw R2, var_a(R0)
           lw R3, var_a(R0)
           mul R1, R2, R3
           sw temp_t5(R0), R1
% processing a + *
           lw R3, var_a(R0)
           lw R2, temp_t5(R0)
           add R1, R3, R2
           sw temp_t6(R0), R1
% processing result = +
           lw R1, temp_t6(R0)
           sw var_result(R0), R1
% writing to console
           lw R1, var_result(R0)
           jl R15, putint
           hlt
% PROGRAM END

% space for var_a
var_a res 4 
% space for var_b
var_b res 4 
% space for var_result
var_result res 4 
% space for constant 5
lit_t1 res 4 
% space for constant 5
lit_t2 res 4 
% space for temp_t3
temp_t3 res 4
% space for temp_t4
temp_t4 res 4
% space for temp_t5
temp_t5 res 4
% space for temp_t6
temp_t6 res 4
