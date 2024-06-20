local resultFlag="0"
local key=KEYS[1]
local addCap=tonumber(ARGV[1])
local phoneInfo=redis.call("HMGET",key,"totalCount","seckillCount")
local total=tonumber(phoneInfo[1])
local alsell=tonumber(phoneInfo[2])
if not total then
	return resultFlag
end
if total >=alsell + addCap then
	local ret=redis.call("HINCRBY",key,"seckillCount",addCap)
	return tostring(ret)
end
return resultFlag

