import os
import glob
import re

# We will run this script to delombok our classes

# Find all java files
files = glob.glob("src/main/java/**/*.java", recursive=True) + glob.glob("src/test/java/**/*.java", recursive=True)

for filepath in files:
    with open(filepath, 'r') as f:
        content = f.read()

    original_content = content
    class_name = os.path.basename(filepath).split('.')[0]

    # Remove lombok imports
    content = re.sub(r'import lombok\..*;\n', '', content)

    # 1. Replace @Slf4j
    if '@Slf4j' in content:
        content = content.replace('@Slf4j\n', '')
        # insert logger inside class
        class_def_regex = r'(public\s+(class|interface)\s+' + class_name + r'[^\{]*\{)'
        logger_stmt = f'\n    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger({class_name}.class);\n'
        content = re.sub(class_def_regex, r'\1' + logger_stmt, content, count=1)

    # 2. Replace @RequiredArgsConstructor
    if '@RequiredArgsConstructor' in content:
        content = content.replace('@RequiredArgsConstructor\n', '')
        
        # find all final fields
        final_fields = re.findall(r'private\s+final\s+([A-Za-z0-9_<>]+)\s+([A-Za-z0-9_]+);', content)
        if final_fields:
            params = ", ".join([f"{t} {n}" for t, n in final_fields])
            assigns = "\n".join([f"        this.{n} = {n};" for t, n in final_fields])
            
            constructor = f"\n    public {class_name}({params}) {{\n{assigns}\n    }}\n"
            
            class_def_regex = r'(public\s+(class|interface)\s+' + class_name + r'[^\{]*\{)'
            content = re.sub(class_def_regex, r'\1' + constructor, content, count=1)

    # 3. Replace @Data / @Builder (for models only)
    if '@Data' in content or '@Builder' in content or '@NoArgsConstructor' in content or '@AllArgsConstructor' in content:
        content = re.sub(r'@Data\n|@Builder\n|@NoArgsConstructor\n|@AllArgsConstructor\n', '', content)
        
        # find all fields
        fields = re.findall(r'private\s+([A-Za-z0-9_<>]+)\s+([A-Za-z0-9_]+);', content)
        
        # getters and setters
        methods = []
        for t, n in fields:
            cap_n = n[0].upper() + n[1:]
            
            # getter
            methods.append(f"    public {t} get{cap_n}() {{ return {n}; }}")
            
            # setter
            methods.append(f"    public void set{cap_n}({t} {n}) {{ this.{n} = {n}; }}")
            
        # builder
        builder_methods = []
        for t, n in fields:
            builder_methods.append(f"        private {t} {n};\n        public {class_name}Builder {n}({t} {n}) {{ this.{n} = {n}; return this; }}")
            
        builder_assigns = "\n".join([f"        instance.set{n[0].upper() + n[1:]}(this.{n});" for t, n in fields])
            
        builder_class = f"""
    public static {class_name}Builder builder() {{ return new {class_name}Builder(); }}
    public static class {class_name}Builder {{
{chr(10).join(builder_methods)}
        public {class_name} build() {{
            {class_name} instance = new {class_name}();
{builder_assigns}
            return instance;
        }}
    }}
"""
        
        # Default constructor
        methods.append(f"    public {class_name}() {{}}")

        content = content[:content.rfind('}')] + "\n" + "\n".join(methods) + "\n" + builder_class + "\n}\n"

    if content != original_content:
        with open(filepath, 'w') as f:
            f.write(content)

print("Delombok finished.")
