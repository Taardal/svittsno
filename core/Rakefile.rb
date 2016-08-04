# Encoding: utf-8

# Script som deployer til test-server. Er avhengig av å ha ruby installert, samt net-ssh og net-scp rubygems. Eks.: 'gem install net-ssh' 

# Kjør 'rake deploy:test' for å utføre
# - Bygg svittsno
# - Stop test-server og fjern gammel versjon
# - Kopier ny svittsno.war
# - Start server med "test"-profil
# - Sjekk at svittsno kjører via rest-kall


require 'net/ssh'
require 'net/scp'
require 'date'

namespace :default do
  puts "Lovlige kommandoer:"
  puts "- rake deploy:test"
end

today = Date.today

api_version = "v1"

test_build_profile = 'test'
test_server_user = ''
test_server_address = ''
test_server_password = ''
test_tomcat_webapps_path = ''
test_tomcat_service = ''
test_tomcat_port = '8080'
test_db_host = 'localhost:3306/svitts'
test_db_user = 'svitts'
test_db_password = ''

current_build_profile = ''
current_server_address = ''
current_server_password = ''
current_server_webapps_path = ''
current_server_tomcat_service = ''
current_server_tomcat_port = ''
current_server_db_host = ''
current_server_db_user = ''
current_server_db_password = ''

namespace :deploy do
  task :test => [:set_test_as_current_server, :build, :cleanup_server, :deploy, :start_server, :ping] do
    puts "Done"
  end
end

task :set_test_as_current_server do
  current_build_profile = test_build_profile
  current_server_address = test_server_address
  current_server_password = test_server_password
  current_server_webapps_path = test_tomcat_webapps_path
  current_server_tomcat_service = test_tomcat_service
  current_server_tomcat_port = test_tomcat_port
  current_server_db_host = test_db_host
  current_server_db_user = test_db_user
  current_server_db_password = test_db_password
end

task :build do
  puts "Building SvittsNO (core) with profile #{current_build_profile} using Maven..."
  puts %x{mvn clean package -P test}
  raise "SvittsNO (core) build failed!" if $?.exitstatus != 0
end

task :cleanup_server do
  puts "Shutting down and cleaning up current server..."
  Net::SSH.start(
      "#{current_server_address}",
      "#{current_server_user}",
      :password => "#{current_server_password}",
      :auth_methods => ['password']
  ) do |ssh|
    ssh.exec!("#{current_server_tomcat_service} stop")
    ssh.exec!("rm -rf #{current_server_webapps_path}/svittsno.war")
    ssh.exec!("rm -rf #{current_server_webapps_path}/svittsno/")
  end
end

task :deploy do
  puts "Deploying SvittsNO (core) to #{current_server_address}..."

  Net::SCP.start(
      "#{current_server_address}",
      "#{current_server_user}",
      :password => "#{current_server_password}",
      :auth_methods => ['password']
  ) do |scp|
    scp.upload!("core/target/svittsno.war", "#{current_server_webapps_path}/")
  end

  Net::SSH.start(
      "#{current_server_address}",
      "#{current_server_user}",
      :password => "#{current_server_password}",
      :auth_methods => ['password']
  ) do |ssh|
    ssh.exec!("chmod 755 #{current_server_webapps_path}/svittsno.war")
  end
end

task :start_server do
  puts "Starting #{current_server_constretto_tag} server..."
  Net::SSH.start(
      "#{current_server_address}",
      "#{current_server_user}",
      :password => "#{current_server_password}",
      :auth_methods => ['password']
  ) do |ssh|
    ssh.exec!("#{current_server_tomcat_service} start")
  end
end

task :ping do
  puts "Is SvittsNO up?"
  time_spent = 0
  while time_spent < 30
    response = %x{curl -s -o /dev/null -w "%{http_code}" 'http://#{current_server_address}:#{current_server_tomcat_port}/api/#{api_version}/selftest'}
    if response == "200"
      break
    end
    sleep(1)
    print "."
    $stdout.flush
    time_spent += 1
  end

  if response != "200"
    fail "SvittsNO is not up (#{response})"
  end

  puts ""
  puts "HTTP response from SvittsNO: #{response}"
end