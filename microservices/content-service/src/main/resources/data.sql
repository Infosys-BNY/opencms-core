INSERT INTO CMS_OFFLINE_STRUCTURE (STRUCTURE_ID, RESOURCE_ID, PARENT_ID, RESOURCE_PATH, STRUCTURE_STATE, DATE_RELEASED, DATE_EXPIRED, STRUCTURE_VERSION) VALUES
('a1b2c3d4-e5f6-4a5b-8c7d-9e0f1a2b3c4d', 'r1b2c3d4-e5f6-4a5b-8c7d-9e0f1a2b3c4d', '00000000-0000-0000-0000-000000000000', '/content/article1.xml', 0, 0, 9223372036854775807, 1),
('a2b2c3d4-e5f6-4a5b-8c7d-9e0f1a2b3c4d', 'r2b2c3d4-e5f6-4a5b-8c7d-9e0f1a2b3c4d', '00000000-0000-0000-0000-000000000000', '/content/article2.xml', 0, 0, 9223372036854775807, 1);

INSERT INTO CMS_OFFLINE_RESOURCES (RESOURCE_ID, RESOURCE_TYPE, RESOURCE_FLAGS, RESOURCE_STATE, RESOURCE_SIZE, DATE_CONTENT, SIBLING_COUNT, DATE_CREATED, DATE_LASTMODIFIED, USER_CREATED, USER_LASTMODIFIED, PROJECT_LASTMODIFIED, RESOURCE_VERSION) VALUES
('r1b2c3d4-e5f6-4a5b-8c7d-9e0f1a2b3c4d', 1, 0, 0, 256, 1699999999000, 1, 1699999999000, 1699999999000, 'admin', 'admin', 'offline', 1),
('r2b2c3d4-e5f6-4a5b-8c7d-9e0f1a2b3c4d', 1, 0, 0, 512, 1699999999000, 1, 1699999999000, 1699999999000, 'admin', 'admin', 'offline', 1);

INSERT INTO CMS_OFFLINE_CONTENTS (RESOURCE_ID, FILE_CONTENT) VALUES
('r1b2c3d4-e5f6-4a5b-8c7d-9e0f1a2b3c4d', 
STRINGDECODE('<?xml version="1.0" encoding="UTF-8"?>\n<Article xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">\n  <Article language="en">\n    <Title><![CDATA[Sample Article 1]]></Title>\n    <Text name="Text0">\n      <links/>\n      <content><![CDATA[<p>This is a sample article content.</p>]]></content>\n    </Text>\n  </Article>\n</Article>')),
('r2b2c3d4-e5f6-4a5b-8c7d-9e0f1a2b3c4d',
STRINGDECODE('<?xml version="1.0" encoding="UTF-8"?>\n<Article xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">\n  <Article language="en">\n    <Title><![CDATA[Sample Article 2]]></Title>\n    <Text name="Text0">\n      <links/>\n      <content><![CDATA[<p>This is another sample article.</p>]]></content>\n    </Text>\n  </Article>\n</Article>'));
