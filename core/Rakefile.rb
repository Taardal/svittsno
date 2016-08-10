# Encoding: utf-8

# Script that deploys the application to a remote server. Depends on Ruby and net-ssh og net-scp rubygems. Ex: 'gem install net-ssh'
# Run 'rake -T' for available tasks

# Running a task will execute the following:
# - Build svitts core module (.war)
# - Stop tomcat on remote server and remove old version (folder and .war-file)
# - Copy new svitts.war (core) to remote server
# - Start tomcat on remote server
# - Ensure that SvittsNO webapp is running by pinging REST API self-test.


require 'net/ssh'
require 'net/scp'
require 'date'

namespace :default do
  puts "rake deploy:test"
  puts "rake deploy:svitts"
end

today = Date.today

api_version = 'v1'

build_profile = ''
server_address = ''
server_user = ''
server_tomcat_service = ''
server_tomcat_port = ''
server_tomcat_webapps_path = ''
server_database_host = ''
server_database_user = ''
server_database_password = ''

test_build_profile = 'test'
test_server_user = 'taardal'
test_server_address = '172.16.42.19'
test_tomcat_service = 'tomcat8'
test_tomcat_port = '8080'
test_tomcat_webapps_path = '/var/lib/tomcat8/webapps'
test_database_host = ''
test_database_user = ''
test_database_password = ''

namespace :deploy do
  # task :test => [:set_test_as_server, :build, :cleanup_server, :deploy, :start_server, :ping] do
  task :test => [:set_test_as_server, :build, :cleanup_server, :deploy, :start_server] do
    puts "Done"
  end
  # task :svitts => [:set_svitts_as_server, :build, :cleanup_server, :deploy, :start_server, :ping] do
  task :svitts => [:set_svitts_as_server, :build] do
    puts "Done"
  end
end

task :set_test_as_server do
  build_profile = test_build_profile
  server_address = test_server_address
  server_user = test_server_user
  server_tomcat_webapps_path = test_tomcat_webapps_path
  server_tomcat_service = test_tomcat_service
  server_tomcat_port = test_tomcat_port
  server_database_host = test_database_host
  server_database_user = test_database_user
  server_database_password = test_database_password
end

task :set_svitts_as_server do
  build_profile = test_build_profile
  server_address = test_server_address
  server_user = test_server_user
  server_tomcat_webapps_path = test_tomcat_webapps_path
  server_tomcat_service = test_tomcat_service
  server_tomcat_port = test_tomcat_port
  server_database_host = test_database_host
  server_database_user = test_database_user
  server_database_password = test_database_password
end

task :build do
  puts "Building SvittsNO (core) with profile #{build_profile} using Maven..."
  puts %x{mvn clean package -P test}
  raise "SvittsNO (core) build failed!" if $?.exitstatus != 0
end

task :cleanup_server do
  puts "Shutting down and cleaning up #{build_profile} server (#{server_address})..."
  Net::SSH.start(
      "#{server_address}",
      "#{server_user}",
      :auth_methods => ['publickey']
  ) do |ssh|
    ssh.exec!("#{server_tomcat_service} stop")
    ssh.exec!("rm -rf #{server_tomcat_webapps_path}/svitts.war")
    ssh.exec!("rm -rf #{server_tomcat_webapps_path}/svitts/")
  end
end

task :deploy do
  puts "Deploying SvittsNO (core) to #{build_profile} server (#{server_address})..."
  Net::SCP.start(
      "#{server_address}",
      "#{server_user}",
      :auth_methods => ['publickey']
  ) do |scp|
    scp.upload!("target/svitts.war", "#{server_tomcat_webapps_path}")
  end
  Net::SSH.start(
      "#{server_address}",
      "#{server_user}",
      :auth_methods => ['publickey']
  ) do |ssh|
    ssh.exec!("chmod 775 #{server_tomcat_webapps_path}/svitts.war")
  end
end

task :start_server do
  puts "Starting #{build_profile} server (#{server_address})..."
  Net::SSH.start(
      "#{server_address}",
      "#{server_user}",
      :auth_methods => ['publickey']
  ) do |ssh|
    ssh.exec!("#{server_tomcat_service} start")
  end
end

task :ping do
  puts "Is SvittsNO up on #{build_profile} server (#{server_address})?"
  time_spent = 0
  response = 0
  while time_spent < 30
    response = %x{curl -s -o /dev/null -w "%{http_code}" 'http://#{server_address}:#{server_tomcat_port}/svitts/api/#{api_version}/selftest'}
    if response == '200'
      break
    end
    sleep(1)
    print "."
    $stdout.flush
    time_spent += 1
  end

  if response != '200'
    fail "SvittsNO is not up (#{response})"
  end

  puts ""
  puts "HTTP response from SvittsNO: #{response}"
end