#!/bin/bash

# Expense Tracker - Setup and Run Script
# This script helps you build and run the Expense Tracker application

set -e

echo "🚀 Expense Tracker - Setup Script"
echo "=================================="
echo ""

# Check Java version
echo "✓ Checking Java version..."
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 21 or later."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -1 | awk -F'"' '{print $2}')
echo "  Found Java version: $JAVA_VERSION"

# Check Maven
echo "✓ Checking Maven..."
if ! command -v mvn &> /dev/null && [ ! -f "./mvnw" ]; then
    echo "❌ Maven is not installed. Please install Maven 3.6+ or use Maven wrapper."
    exit 1
fi

MVN_CMD="./mvnw"
if [ ! -f "$MVN_CMD" ]; then
    MVN_CMD="mvn"
fi

echo "  Using: $MVN_CMD"
echo ""

# Build the project
echo "📦 Building the project..."
echo "   This may take a few minutes on first run..."
$MVN_CMD clean package -q
echo "✓ Build successful!"
echo ""

# Display next steps
echo "✅ Setup Complete!"
echo ""
echo "📝 Next Steps:"
echo "   1. Start the application:"
echo "      $MVN_CMD spring-boot:run"
echo ""
echo "   2. Once running, open in your browser:"
echo "      http://localhost:8080/swagger-ui.html"
echo ""
echo "   3. Test the API directly from Swagger UI"
echo ""
echo "📚 Documentation:"
echo "   - Quick Start:          QUICK_START.md"
echo "   - Implementation Guide: IMPLEMENTATION_GUIDE.md"
echo "   - API Docs:            http://localhost:8080/v3/api-docs"
echo "   - H2 Console:          http://localhost:8080/h2-console"
echo ""
echo "💡 Tips:"
echo "   - Default database: H2 in-memory (no setup required)"
echo "   - To use PostgreSQL: Update src/main/resources/application.yml"
echo "   - Data resets on application restart (H2)"
echo ""
echo "Happy coding! 🎉"
