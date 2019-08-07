CREATE TABLE IF EXISTS Matchs (
id serial primary key,
name_match character varying (120),
liga_match character varying (120),
result_match character varying (120)
);

CREATE TABLE IF EXISTS Rates (
id serial primary key,
match_id integer references Matchs(id)
date_scan character varying (20),
p1 float4,
p2 float4,
1Х float4,
2Х float4,
Х float4,
12 float4,
ТМ float4,
ТМB float4,
ТМM float4,
fora1 float4,
koef_f1 float4,
fora2 float4,
koef_f2 float4,
ind_tot1 float4,
it1b float4, 
it1m float4,
ind_tot2 float4,
it2b float4, 
it2m float4,
comment text
);

CREATE TABLE IF EXISTS Archive (
id serial primary key,
date_html character varying (10),
html text
);
