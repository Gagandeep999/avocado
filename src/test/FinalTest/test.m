% processing t1 stores 0
           addi R1, R0, 0
           sw lit_t1(R0), R1
% processing t2 stores 0
           addi R1, R0, 0
           sw lit_t2(R0), R1
% processing result = 0
           lw R1, lit_t2(R0)
           sw var_result(R0), R1
% processing a mul/div/and a
           lw R2, (R0)
           lw R3, (R0)
           mul R1, R2, R3
           sw temp_t3(R0), R1
% processing * add/sub/or ITEM
           lw R3, temp_t3(R0)
           lw R2, (R0)
           add R1, R3, R2
           sw temp_t4(R0), R1
% processing result = +
           lw R1, temp_t4(R0)
           sw var_result(R0), R1
% processing result = result
           lw R1, var_result(R0)
           sw var_result(R0), R1
% processing result mul/div/and result
           lw R2, var_result(R0)
           lw R3, var_result(R0)
           mul R1, R2, R3
           sw temp_t5(R0), R1
% processing * add/sub/or ITEM
           lw R3, temp_t5(R0)
           lw R2, (R0)
           add R1, R3, R2
           sw temp_t6(R0), R1
% processing result = +
           lw R1, temp_t6(R0)
           sw var_result(R0), R1
% processing result mul/div/and result
           lw R2, var_result(R0)
           lw R3, var_result(R0)
           mul R1, R2, R3
           sw temp_t7(R0), R1
% processing * add/sub/or ITEM
           lw R3, temp_t7(R0)
           lw R2, (R0)
           add R1, R3, R2
           sw temp_t8(R0), R1
% processing result = +
           lw R1, temp_t8(R0)
           sw var_result(R0), R1
% processing n = n
           lw R1, var_n(R0)
           sw var_n(R0), R1
% processing t9 stores 0
           addi R1, R0, 0
           sw lit_t9(R0), R1
% processing i = 0
           lw R1, lit_t9(R0)
           sw var_i(R0), R1
% processing t10 stores 0
           addi R1, R0, 0
           sw lit_t10(R0), R1
% processing j = 0
           lw R1, lit_t10(R0)
           sw var_j(R0), R1
% processing t11 stores 0
           addi R1, R0, 0
           sw lit_t11(R0), R1
% processing temp = 0
           lw R1, lit_t11(R0)
           sw var_temp(R0), R1
% processing t12 stores 1
           addi R1, R0, 1
           sw lit_t12(R0), R1
% processing n add/sub/or 1
           lw R2, var_n(R0)
           lw R3, lit_t12(R0)
           sub R1, R2, R3
           sw temp_t13(R0), R1
% processing i comparison -
           lw R3, var_i(R0)
           lw R2, temp_t13(R0)
           clt R1, R3, R2
           sw temp_t14(R0), R1
% processing n add/sub/or n
           lw R2, var_n(R0)
           lw R3, var_n(R0)
           sub R1, R2, R3
           sw temp_t15(R0), R1
% processing t16 stores 1
           addi R1, R0, 1
           sw lit_t16(R0), R1
% processing - add/sub/or 1
           lw R3, temp_t15(R0)
           lw R2, lit_t16(R0)
           sub R1, R3, R2
           sw temp_t17(R0), R1
% processing j comparison -
           lw R2, var_j(R0)
           lw R3, temp_t17(R0)
           clt R1, R2, R3
           sw temp_t18(R0), R1
% processing t19 stores 1
           addi R3, R0, 1
           sw lit_t19(R0), R3
% processing j add/sub/or 1
           lw R2, var_j(R0)
           lw R4, lit_t19(R0)
           add R3, R2, R4
           sw temp_t20(R0), R3
% processing arr comparison arr
           lw R4, param_arr(R0)
           lw R2, param_arr(R0)
           sw temp_t21(R0), R3
% processing if condition 
if1           lw R1, temp_t21(R0)
           bz R1, if1_else 
% processing temp = temp
           lw R1, var_temp(R0)
           sw var_temp(R0), R1
% processing t22 stores 1
           addi R1, R0, 1
           sw lit_t22(R0), R1
% processing j add/sub/or 1
           lw R3, var_j(R0)
           lw R2, lit_t22(R0)
           add R1, R3, R2
           sw temp_t23(R0), R1
% processing arr = arr
           lw R1, param_arr(R0)
           sw param_arr(R0), R1
% processing t24 stores 1
           addi R1, R0, 1
           sw lit_t24(R0), R1
% processing j add/sub/or 1
           lw R2, var_j(R0)
           lw R3, lit_t24(R0)
           add R1, R2, R3
           sw temp_t25(R0), R1
% processing arr = arr
           lw R1, param_arr(R0)
           sw param_arr(R0), R1
           j if1_end 
if1_else           nop
if1_end           nop
% processing t26 stores 1
           addi R1, R0, 1
           sw lit_t26(R0), R1
% processing j add/sub/or 1
           lw R3, var_j(R0)
           lw R2, lit_t26(R0)
           add R1, R3, R2
           sw temp_t27(R0), R1
% processing j = +
           lw R1, temp_t27(R0)
           sw var_j(R0), R1
% processing t28 stores 1
           addi R1, R0, 1
           sw lit_t28(R0), R1
% processing i add/sub/or 1
           lw R2, var_i(R0)
           lw R3, lit_t28(R0)
           add R1, R2, R3
           sw temp_t29(R0), R1
% processing i = +
           lw R1, temp_t29(R0)
           sw var_i(R0), R1
% processing t30 stores 0
           addi R1, R0, 0
           sw lit_t30(R0), R1
% processing i = 0
           lw R1, lit_t30(R0)
           sw var_i(R0), R1
% writing to console
           lw R1, var_i(R0)
           jl R15, putint
% PROGRAM START
           entry
% processing t31 stores 2
           addi R1, R0, 2
           sw lit_t31(R0), R1
% processing t32 stores 3
           addi R1, R0, 3
           sw lit_t32(R0), R1
% processing t33 stores 2
           addi R3, R0, 2
           sw lit_t33(R0), R3
% processing x comparison 2
           lw R2, var_x(R0)
           lw R4, lit_t33(R0)
           ceq R3, R2, R4
           sw temp_t34(R0), R3
% processing if condition 
if1           lw R1, temp_t34(R0)
           bz R1, if1_else 
% processing t35 stores 1
           addi R1, R0, 1
           sw lit_t35(R0), R1
% processing x add/sub/or 1
           lw R3, var_x(R0)
           lw R4, lit_t35(R0)
           add R1, R3, R4
           sw temp_t36(R0), R1
% writing to console
           lw R1, temp_t36(R0)
           jl R15, putint
           j if1_end 
if1_else           nop
% processing t37 stores 1
           addi R1, R0, 1
           sw lit_t37(R0), R1
% processing x add/sub/or 1
           lw R4, var_x(R0)
           lw R3, lit_t37(R0)
           sub R1, R4, R3
           sw temp_t38(R0), R1
% writing to console
           lw R1, temp_t38(R0)
           jl R15, putint
if1_end           nop
           hlt
% PROGRAM END

% space for var_a
var_a res 4 
% space for var_b
var_b res 4 
% space for var_a
var_a res 4 
% space for var_b
var_b res 4 
% space for var_c
var_c res 4 
% space for constant 0
lit_t1 res 4 
% space for var_result
var_result res 4 
% space for constant 0
lit_t2 res 4 
% space for temp_t3
temp_t3 res 4
% space for temp_t4
temp_t4 res 4
% space for var_result
var_result res 4 
% space for temp_t5
temp_t5 res 4
% space for temp_t6
temp_t6 res 4
% space for temp_t7
temp_t7 res 4
% space for temp_t8
temp_t8 res 4
% space for var_n
var_n res 4 
% space for var_i
var_i res 4 
% space for var_j
var_j res 4 
% space for var_temp
var_temp res 4 
% space for constant 0
lit_t9 res 4 
% space for constant 0
lit_t10 res 4 
% space for constant 0
lit_t11 res 4 
% space for constant 1
lit_t12 res 4 
% space for temp_t13
temp_t13 res 4
% space for temp_t14
temp_t14 res 4
% space for temp_t15
temp_t15 res 4
% space for constant 1
lit_t16 res 4 
% space for temp_t17
temp_t17 res 4
% space for temp_t18
temp_t18 res 4
% space for constant 1
lit_t19 res 4 
% space for temp_t20
temp_t20 res 4
% space for temp_t21
temp_t21 res 4
% space for constant 1
lit_t22 res 4 
% space for temp_t23
temp_t23 res 4
% space for constant 1
lit_t24 res 4 
% space for temp_t25
temp_t25 res 4
% space for constant 1
lit_t26 res 4 
% space for temp_t27
temp_t27 res 4
% space for constant 1
lit_t28 res 4 
% space for temp_t29
temp_t29 res 4
% space for var_i
var_i res 4 
% space for constant 0
lit_t30 res 4 
% space for var_x
var_x res 4 
% space for constant 2
lit_t31 res 4 
% space for constant 3
lit_t32 res 4 
% space for constant 2
lit_t33 res 4 
% space for temp_t34
temp_t34 res 4
% space for constant 1
lit_t35 res 4 
% space for temp_t36
temp_t36 res 4
% space for constant 1
lit_t37 res 4 
% space for temp_t38
temp_t38 res 4
