--
--    Copyright 2015-2017 the original author or authors.
--
--    Licensed under the Apache License, Version 2.0 (the "License");
--    you may not use this file except in compliance with the License.
--    You may obtain a copy of the License at
--
--       http://www.apache.org/licenses/LICENSE-2.0
--
--    Unless required by applicable law or agreed to in writing, software
--    distributed under the License is distributed on an "AS IS" BASIS,
--    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--    See the License for the specific language governing permissions and
--    limitations under the License.
--

DROP schema stock_schema;
create schema stock_schema;
DROP TABLE stock_schema.stocks;

CREATE TABLE stock_schema.stock (
  symbol varchar NULL,
  name varchar NULL
)
WITH (
OIDS=FALSE
) ;


CREATE TABLE stock_schema.quote (
  symbol varchar NULL,
  openPrice float8 NULL,
  closePrice float8 NULL,
  lowPrice float8 NULL,
  highPrice float8 NULL,
  beginsAt timestamptz NULL,
  closesAt timestamptz NULL,
  volume int8 NULL,
  createdAt timestamptz NULL DEFAULT now(),
    CONSTRAINT quote_pk PRIMARY KEY (symbol, beginsat, closesat)
)
WITH (
OIDS=FALSE
) ;
